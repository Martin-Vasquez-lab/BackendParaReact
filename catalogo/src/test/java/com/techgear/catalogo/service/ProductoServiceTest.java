package com.techgear.catalogo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private ProductoService productoService;


    @Mock
    private ProductoRepository productoRepository;
    
    Categoria categoria = new Categoria();
    
    @Test
    public void testFindAll() {
        when(productoRepository.findAll()).thenReturn(List.of(new Producto(1, "Test",10,100,categoria)));
        List<Producto> productos = productoRepository.findAll();
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindById() {
        int id = 1;
        Producto producto = new Producto(1, "Test",10,100,categoria);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        Producto found = productoService.getProducto(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Producto producto = new Producto(1, "Test",10,100,categoria);
        when(productoRepository.save(producto)).thenReturn(producto);
        Producto saved = productoService.saveProducto(producto);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test 
    public void testDeleteByCodigo() {
        Integer id = 1;
        doNothing().when(productoRepository).deleteById(id);
        productoService.deleteProducto(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

}
