package com.techgear.catalogo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.service.CategoriaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Testing");
        categoria.setDescripcion("Testing TechGear");
    }

    @Test //Get All
    public void testGetAllCategorias() throws Exception {
        when(categoriaService.getCategorias()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Testing"))
                .andExpect(jsonPath("$[0].descripcion").value("Testing TechGear"));
    }

    @Test //GetById
    public void testGetCategoriaById() throws Exception {
        when(categoriaService.getCategoria(1)).thenReturn(categoria);

        mockMvc.perform(get("/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.descripcion").value("Testing TechGear"));
    }

    @Test //Post
    public void testCreateCategoria() throws Exception {
        when(categoriaService.saveCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.descripcion").value("Testing TechGear"));
    }

    @Test //Update
    public void testUpdateCategoria() throws Exception {
        when(categoriaService.updateCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.descripcion").value("Testing TechGear"));
    }

    @Test
    public void testDeleteCategoria() throws Exception {
        doNothing().when(categoriaService).deleteCategoria(1);

        mockMvc.perform(delete("/categoria/1"))
                .andExpect(status().isNoContent());
        verify(categoriaService, times(1)).deleteCategoria(1);
    }

}
