package com.techgear.usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.model.Usuario;
import com.techgear.usuario.service.UsuarioService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
public class UsuarioControllerTest {

    @Autowired // Proporciona una manera de realizar peticiones HTTP en las pruebas
    private MockMvc mockMvc;

    @MockBean// Crea un mock del servicio de <Rol>
    private UsuarioService usuarioService;

    @Autowired // Se usa para convertir objetos Java a JSON y viceversa
    private ObjectMapper objectMapper;
    private Usuario usuario;
    Rol rolMock = new Rol(1, "ADMIN");

    @BeforeEach // Se elabora un objeto como prueba del Testing
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setRut("19.615.206-7");
        usuario.setNombre("Testing");
        usuario.setCorreo("gigor3001@gmail.com");
        usuario.setTelefono(975445991);
        usuario.setContrasena("test1234");
        usuario.setRol(rolMock);
    }

    @Test //Get All
    public void testGetAllUsuarios() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rut").value("19.615.206-7"))
                .andExpect(jsonPath("$[0].nombre").value("Testing"))
                .andExpect(jsonPath("$[0].correo").value("gigor3001@gmail.com"))
                .andExpect(jsonPath("$[0].telefono").value(975445991))
                .andExpect(jsonPath("$[0].contrasena").value("test1234"))
                .andExpect(jsonPath("$[0].rol").value(rolMock));
    }

    @Test //GetById
    public void testGetUsuarioById() throws Exception {
        when(usuarioService.getUsuario(1)).thenReturn(usuario);

        mockMvc.perform(get("/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("19.615.206-7"))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.correo").value("gigor3001@gmail.com"))
                .andExpect(jsonPath("$.telefono").value(975445991))
                .andExpect(jsonPath("$.contrasena").value("test1234"))
                .andExpect(jsonPath("$.rol").value(rolMock));
    }

    
    @Test //Post
    public void testCreateUsuario() throws Exception {
        when(usuarioService.saveUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("19.615.206-7"))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.correo").value("gigor3001@gmail.com"))
                .andExpect(jsonPath("$.telefono").value(975445991))
                .andExpect(jsonPath("$.contrasena").value("test1234"))
                .andExpect(jsonPath("$.rol").value(rolMock));
    }

    @Test //Update
    public void testUpdateUsuario() throws Exception {
        when(usuarioService.updateUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("19.615.206-7"))
                .andExpect(jsonPath("$.nombre").value("Testing"))
                .andExpect(jsonPath("$.correo").value("gigor3001@gmail.com"))
                .andExpect(jsonPath("$.telefono").value(975445991))
                .andExpect(jsonPath("$.contrasena").value("test1234"))
                .andExpect(jsonPath("$.rol").value(rolMock));
    }

    @Test
    public void testDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(1);

        mockMvc.perform(delete("/usuario/1"))
                .andExpect(status().isNoContent());
        verify(usuarioService, times(1)).deleteUsuario(1);
    }

}
