package com.techgear.catalogo.controller;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.service.ProductoService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;
    private Producto producto;
    Categoria categoria = new Categoria();

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Testing");
        producto.setPrecio(1000);
        producto.setStock(10);
        producto.setCategoria(categoria);
    }

    @Test //Get All
    public void testGetAllProductos() throws Exception {
        when(productoService.getProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Testing"))
                .andExpect(jsonPath("$[0].precio").value(1000))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].categoria").value(categoria));
    }

    @Test //GetById
    public void testGetProductoById() throws Exception {
        when(productoService.getProducto(1)).thenReturn(producto);

        mockMvc.perform(get("/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.precio").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.categoria").value(categoria));
    }

    @Test //Post
    public void testCreateProducto() throws Exception {
        when(productoService.saveProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.precio").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.categoria").value(categoria));
    }

    @Test //Update
    public void testUpdateProducto() throws Exception {
        when(productoService.updateProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.precio").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.categoria").value(categoria));
    }

    @Test
    public void testDeleteProducto() throws Exception {
        doNothing().when(productoService).deleteProducto(1);

        mockMvc.perform(delete("/producto/1"))
                .andExpect(status().isNoContent());
        verify(productoService, times(1)).deleteProducto(1);
    }

}
