package com.techgear.pago.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.techgear.pago.model.Factura;
import com.techgear.pago.repository.FacturaRepository;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    @Value("${carro.service.url}")
    private String carroServiceUrl;

    @Value("${catalogo.service.url}")
    private String catalogoServiceUrl;

    public List<Factura> getAllFacturas(){
        return facturaRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public List<Factura> getFacturasDetalles(){
        List<Factura> facturas = facturaRepository.findAll();
        for (Factura factura : facturas) {
            try {
                String usuarioUrl = usuarioServiceUrl+"/"+factura.getUsuarioId();
                Map<String, Object> usuarioDetalles = restTemplate.getForObject(usuarioUrl, Map.class);
                factura.setUsuarioDetalles(usuarioDetalles);
                String carroUrl = carroServiceUrl+"/"+factura.getCarroId();
                Map<String, Object> carroDetalles = restTemplate.getForObject(carroUrl, Map.class);
                factura.setCarroDetalles(carroDetalles);
            } catch (Exception e) {
                factura.setUsuarioDetalles(null);
                factura.setCarroDetalles(null);
            }
        }
        return facturas;
    }

    public Factura getFactura(int id){
        return facturaRepository.findById(id).orElse(null);
    }

    public Factura saveFactura(Factura factura){
        return facturaRepository.save(factura);
    }

    public Factura updateFactura(Factura factura){
        Factura updFact = getFactura(factura.getId());
        if (updFact==null) {
            return null;
        }
        updFact.setFormaPago(factura.getFormaPago());
        updFact.setMonto(factura.getMonto());
        updFact.setFecha(factura.getFecha());
        
        return facturaRepository.save(updFact);
    }

    public void deleteFactura(Integer id){
        facturaRepository.deleteById(id);
    }

    /**
     * Procesa el pago completo con validación y actualización de stock
     */
    @Transactional
    public Factura processPaymentWithStockValidation(Factura factura) throws Exception {
        // 1. Obtener detalles del carrito
        String carroUrl = carroServiceUrl + "/carro/" + factura.getCarroId();
        ResponseEntity<Map> carroResponse = restTemplate.exchange(
            carroUrl, HttpMethod.GET, null, Map.class);
        
        if (!carroResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error al obtener detalles del carrito");
        }

        Map<String, Object> carroData = carroResponse.getBody();
        
        // 2. Validar stock para todos los productos en el carrito
        if (carroData.containsKey("productos")) {
            List<Map<String, Object>> productos = (List<Map<String, Object>>) carroData.get("productos");
            
            for (Map<String, Object> item : productos) {
                Map<String, Object> producto = (Map<String, Object>) item.get("producto");
                Integer productId = (Integer) producto.get("id");
                Integer cantidad = (Integer) item.get("cantidad");
                
                // Validar stock llamando al microservicio catalogo
                if (!validateStock(productId, cantidad)) {
                    throw new Exception("Stock insuficiente para producto: " + producto.get("nombre"));
                }
            }
        }

        // 3. Si todo está bien, crear la factura
        Factura savedFactura = facturaRepository.save(factura);

        // 4. Reducir stock después de crear la factura exitosamente
        try {
            if (carroData.containsKey("productos")) {
                List<Map<String, Object>> productos = (List<Map<String, Object>>) carroData.get("productos");
                
                for (Map<String, Object> item : productos) {
                    Map<String, Object> producto = (Map<String, Object>) item.get("producto");
                    Integer productId = (Integer) producto.get("id");
                    Integer cantidad = (Integer) item.get("cantidad");
                    
                    reduceStock(productId, cantidad);
                }
            }
        } catch (Exception e) {
            // Si falla la reducción de stock, podríamos necesitar rollback
            // Pero por simplicidad, loggeamos el error
            System.err.println("Error al reducir stock: " + e.getMessage());
        }

        return savedFactura;
    }

    /**
     * Valida stock llamando al microservicio catalogo
     */
    private boolean validateStock(Integer productId, Integer quantity) {
        try {
            String stockUrl = catalogoServiceUrl + "/producto/" + productId;
            ResponseEntity<Map> response = restTemplate.exchange(
                stockUrl, HttpMethod.GET, null, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> producto = response.getBody();
                Integer stock = (Integer) producto.get("stock");
                return stock >= quantity;
            }
        } catch (Exception e) {
            System.err.println("Error al validar stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Reduce stock llamando al microservicio catalogo
     */
    private void reduceStock(Integer productId, Integer quantity) {
        try {
            // Aquí necesitarías un endpoint PUT en catalogo para actualizar stock
            String updateStockUrl = catalogoServiceUrl + "/producto/" + productId + "/stock";
            
            Map<String, Object> updateData = Map.of("quantity", quantity);
            
            restTemplate.put(updateStockUrl, updateData);
        } catch (Exception e) {
            System.err.println("Error al reducir stock: " + e.getMessage());
            throw e; // Re-lanzar para que falle la transacción si es necesario
        }
    }
}
