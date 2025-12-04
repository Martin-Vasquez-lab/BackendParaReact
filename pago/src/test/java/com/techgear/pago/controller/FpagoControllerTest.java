package com.techgear.pago.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.techgear.pago.model.Fpago;
import com.techgear.pago.service.FpagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(FpagoController.class)
public class FpagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FpagoService fpagoService;

    @Autowired
    private ObjectMapper objectMapper;
    private Fpago fpago;

    @BeforeEach
    void setUp() {
        fpago = new Fpago();
        fpago.setId(1);
        fpago.setNombreFpago("Test");
    }

    @Test //Get All
    public void testGetAllProductos() throws Exception {
        when(fpagoService.getAllFpagos()).thenReturn(List.of(fpago));

        mockMvc.perform(get("/fpago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreFpago").value("Test"));
    }

    @Test //GetById
    public void testGetProductoById() throws Exception {
        when(fpagoService.getFpago(1)).thenReturn(fpago);

        mockMvc.perform(get("/fpago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreFpago").value("Test"));
    }

    @Test //Post
    public void testCreateProducto() throws Exception {
        when(fpagoService.saveFpago(any(Fpago.class))).thenReturn(fpago);

        mockMvc.perform(post("/fpago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fpago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreFpago").value("Test"));
    }

    @Test //Update
    public void testUpdateProducto() throws Exception {
        when(fpagoService.updateFpago(any(Fpago.class))).thenReturn(fpago);

        mockMvc.perform(put("/fpago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fpago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreFpago").value("Test"));
    }

    @Test
    public void testDeleteProducto() throws Exception {
        doNothing().when(fpagoService).deleteFpago(1);

        mockMvc.perform(delete("/fpago/1"))
                .andExpect(status().isNoContent());
        verify(fpagoService, times(1)).deleteFpago(1);
    }

}
