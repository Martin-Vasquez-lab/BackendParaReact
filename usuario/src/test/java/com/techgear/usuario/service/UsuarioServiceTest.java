package com.techgear.usuario.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.model.Usuario;
import com.techgear.usuario.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;


    @Mock
    private UsuarioRepository usuarioRepository;
    
    Rol rol = new Rol();

    @Test
    public void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(1, "19.615.206-7","Tester","gigor3001@gmail.com",975445001,"test1234",rol)));
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Usuario usuario = new Usuario(id, "19.615.206-7","Tester","gigor3001@gmail.com",975445001,"test1234",rol);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.getUsuario(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Usuario usuario = new Usuario(1, "19.615.206-7","Tester","gigor3001@gmail.com",975445001,"test1234",rol);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario saved = usuarioService.saveUsuario(usuario);
        assertNotNull(saved);
        assertEquals("Tester", saved.getNombre());
    }

    @Test 
    public void testDeleteById() {
        Integer id = 1;
        doNothing().when(usuarioRepository).deleteById(id);
        usuarioService.deleteUsuario(id);
        verify(usuarioRepository, times(1)).deleteById(id);
    }

}
