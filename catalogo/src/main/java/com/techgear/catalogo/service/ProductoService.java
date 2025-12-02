package com.techgear.catalogo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techgear.catalogo.exception.StockValidationException;
import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProductos(){
        return productoRepository.findAll();
    }

    public Producto getProducto(int id){
        return productoRepository.findById(id).orElse(null);
    }

    public Producto saveProducto(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto updateProducto(Producto producto){
        Producto updproducto = getProducto(producto.getId());
        if (updproducto==null) {
            return null;
        }
        updproducto.setNombre(producto.getNombre());
        updproducto.setPrecio(producto.getPrecio());
        updproducto.setStock(producto.getStock());
        updproducto.setCategoria(producto.getCategoria());
        
        return productoRepository.save(updproducto);
    }

    public void deleteProducto(Integer id){
        productoRepository.deleteById(id);
    }      

    /**
     * Valida si hay suficiente stock para una cantidad específica
     */
    public boolean hasEnoughStock(Integer productId, Integer quantity) {
        Optional<Producto> productoOpt = productoRepository.findById(productId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            return producto.getStock() >= quantity;
        }
        return false;
    }

    /**
     * Reserva stock temporalmente (útil para carritos)
     */
    @Transactional
    public boolean reserveStock(Integer productId, Integer quantity) {
        Optional<Producto> productoOpt = productoRepository.findById(productId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            if (producto.getStock() >= quantity) {
                // Aquí podrías implementar lógica de reserva temporal
                // Por simplicidad, solo validamos disponibilidad
                return true;
            }
        }
        return false;
    }

    /**
     * Reduce stock después de una compra exitosa
     */
    @Transactional
    public boolean reduceStock(Integer productId, Integer quantity) {
        Optional<Producto> productoOpt = productoRepository.findById(productId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            if (producto.getStock() >= quantity) {
                producto.setStock(producto.getStock() - quantity);
                productoRepository.save(producto);
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza stock manualmente (para administración)
     */
    @Transactional
    public Producto updateStock(Integer productId, Integer newStock) {
        Optional<Producto> productoOpt = productoRepository.findById(productId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(newStock);
            return productoRepository.save(producto);
        }
        throw new StockValidationException("Producto no encontrado");
    }
}
