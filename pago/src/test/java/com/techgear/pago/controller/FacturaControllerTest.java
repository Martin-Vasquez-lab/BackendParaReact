package com.techgear.pago.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.techgear.pago.model.Factura;
import com.techgear.pago.model.Fpago;
import com.techgear.pago.service.FacturaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(FacturaController.class)
public class FacturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacturaService facturaService;

    @Autowired
    private ObjectMapper objectMapper;
    private Factura factura;
    Fpago fpago = new Fpago();
    
    String fechaStr = "1969-12-31";
    java.sql.Date fechaSql = java.sql.Date.valueOf(fechaStr);

    @BeforeEach
    void setUp() {
        factura = new Factura();
        factura.setId(1);
        factura.setCarroId(1);
        factura.setUsuarioId(1);
        factura.setFecha(fechaSql);
        factura.setMonto(1000);
        factura.setFormaPago(fpago);
    }

    @Test //Get All
    public void testGetAllProductos() throws Exception {
        when(facturaService.getAllFacturas()).thenReturn(List.of(factura));

        mockMvc.perform(get("/factura"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].carroId").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andExpect(jsonPath("$[0].fecha").value("1969-12-31"))
                .andExpect(jsonPath("$[0].monto").value(1000))
                .andExpect(jsonPath("$[0].formaPago").value(fpago));
    }

    @Test //GetById
    public void testGetProductoById() throws Exception {
        when(facturaService.getFactura(1)).thenReturn(factura);

        mockMvc.perform(get("/factura/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.carroId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.fecha").value("1969-12-31"))
                .andExpect(jsonPath("$.monto").value(1000))
                .andExpect(jsonPath("$.formaPago").value(fpago));
    }

    @Test //Post
    public void testCreateProducto() throws Exception {
        when(facturaService.saveFactura(any(Factura.class))).thenReturn(factura);

        mockMvc.perform(post("/factura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.carroId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.fecha").value("1969-12-31"))
                .andExpect(jsonPath("$.monto").value(1000))
                .andExpect(jsonPath("$.formaPago").value(fpago));
    }

    @Test //Update
    public void testUpdateProducto() throws Exception {
        when(facturaService.updateFactura(any(Factura.class))).thenReturn(factura);

        mockMvc.perform(put("/factura")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.carroId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.fecha").value("1969-12-31"))
                .andExpect(jsonPath("$.monto").value(1000))
                .andExpect(jsonPath("$.formaPago").value(fpago));
    }

    @Test
    public void testDeleteProducto() throws Exception {
        doNothing().when(facturaService).deleteFactura(1);

        mockMvc.perform(delete("/factura/1"))
                .andExpect(status().isNoContent());
        verify(facturaService, times(1)).deleteFactura(1);
    }

}
