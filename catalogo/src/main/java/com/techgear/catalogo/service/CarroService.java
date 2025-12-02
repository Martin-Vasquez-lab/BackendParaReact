package com.techgear.catalogo.service;

import java.util.List;
import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techgear.catalogo.model.Carro;
import com.techgear.catalogo.repository.CarroRepository;

@Service
public class CarroService {

    // Llamado de dependencias
    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URLs microservicios
    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    public CarroService(CarroRepository carroRepository){
        this.carroRepository= carroRepository;
    }

    // Métodos
    public List<Carro> getCarros(){
        return carroRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public List<Carro> getCarrosDetalles(){
        List<Carro> carros = carroRepository.findAll();
        for (Carro carro : carros) {
            try {
                String usuarioUrl = usuarioServiceUrl+"/"+carro.getUsuarioId();
                Map<String, Object> usuarioDetalles = restTemplate.getForObject(usuarioUrl, Map.class);
                carro.setUsuarioDetalles(usuarioDetalles);
            } catch (Exception e) {
                carro.setUsuarioDetalles(null); 
            }
        }
        return carros;
    }

    public Carro getCarro(int id){
        return carroRepository.findById(id).orElse(null);
    }

    public Carro saveCarro(Carro carro){
        return carroRepository.save(carro);
    }

    public Carro updateCarro(Carro carro){
        Carro updcarro = getCarro(carro.getId());
        if (updcarro==null) {
            return null;
        }
        updcarro.setProducto(carro.getProducto());
        updcarro.setCantidad(carro.getCantidad());
        
        return carroRepository.save(updcarro);
    }

    public void deleteCarro(Integer id){
        carroRepository.deleteById(id);
    }      
}
