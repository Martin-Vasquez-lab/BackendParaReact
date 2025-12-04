package com.techgear.usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.service.RolService;

import java.util.List;

@WebMvcTest(RolController.class) // Se realiza una prueba con el controlador <Rol>
public class RolControllerTest {

    @Autowired // Proporciona una manera de realizar peticiones HTTP en las pruebas
    private MockMvc mockMvc;

    @MockBean// Crea un mock del servicio de <Rol>
    private RolService rolService;

    @Autowired // Se usa para convertir objetos Java a JSON y viceversa
    private ObjectMapper objectMapper;

    private Rol rol;

    @BeforeEach // Se elabora un objeto como prueba del Testing
    void setUp() {
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("Testing");
    }

    @Test //Get All
    public void testGetAllRoles() throws Exception {
        when(rolService.getAllRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/rol"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Testing"));
    }

    @Test //GetById
    public void testGetRolById() throws Exception {
        when(rolService.getRol(1)).thenReturn(rol);

        mockMvc.perform(get("/rol/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"));
    }

    @Test //Post
    public void testCreateRol() throws Exception {
        when(rolService.saveRol(any(Rol.class))).thenReturn(rol);

        mockMvc.perform(post("/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"));
    }

    @Test //Update
    public void testUpdateRol() throws Exception {
        when(rolService.updateRol(any(Rol.class))).thenReturn(rol);

        mockMvc.perform(put("/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Testing"));
    }

    @Test
    public void testDeleteRol() throws Exception {
        doNothing().when(rolService).deleteRol(1);
        mockMvc.perform(delete("/rol/1"))
                .andExpect(status().isNoContent());
        verify(rolService, times(1)).deleteRol(1);
    }
  
}