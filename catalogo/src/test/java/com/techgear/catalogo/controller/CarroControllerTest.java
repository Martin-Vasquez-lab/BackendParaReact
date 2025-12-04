package com.techgear.catalogo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.techgear.catalogo.model.Carro;
import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.service.CarroService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CarroController.class)
public class CarroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarroService carroService;

    @Autowired
    private ObjectMapper objectMapper;
    private Carro carro;
    Producto prodMock = new Producto();

    @BeforeEach
    void setUp() {
        carro = new Carro();
        carro.setId(1);
        carro.setCantidad(10);
        carro.setProducto(prodMock);
        carro.setUsuarioId(1);
    }

    @Test
    public void testGetAllCarros() throws Exception {
        when(carroService.getCarros()).thenReturn(List.of(carro));

        mockMvc.perform(get("/carro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(10))
                .andExpect(jsonPath("$[0].producto").value(prodMock))
                .andExpect(jsonPath("$[0].usuarioId").value(1));
    }

    @Test 
    public void testGetCarroById() throws Exception {
        when(carroService.getCarro(1)).thenReturn(carro);

        mockMvc.perform(get("/carro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.producto").value(prodMock))
                .andExpect(jsonPath("$.usuarioId").value(1));
    }

    @Test
    public void testCreateCarro() throws Exception {
        when(carroService.saveCarro(any(Carro.class))).thenReturn(carro);

        mockMvc.perform(post("/carro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.producto").value(prodMock))
                .andExpect(jsonPath("$.usuarioId").value(1));
    }

    @Test
    public void testUpdateCarro() throws Exception {
        when(carroService.updateCarro(any(Carro.class))).thenReturn(carro);

        mockMvc.perform(put("/carro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.producto").value(prodMock))
                .andExpect(jsonPath("$.usuarioId").value(1));
    }

    @Test
    public void testDeleteCarro() throws Exception {
        doNothing().when(carroService).deleteCarro(1);

        mockMvc.perform(delete("/carro/1"))
                .andExpect(status().isNoContent());
        verify(carroService, times(1)).deleteCarro(1);
    }

}
