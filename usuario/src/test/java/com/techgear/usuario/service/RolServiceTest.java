package com.techgear.usuario.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.repository.RolRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private RolService rolService;


    @Mock
    private RolRepository rolRepository;
    

    @Test
    public void testFindAll() {
        when(rolRepository.findAll()).thenReturn(List.of(new Rol(1, "Tester")));
        List<Rol> roles = rolRepository.findAll();
        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Rol rol = new Rol(id, "Tester");
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));
        Rol found = rolService.getRol(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Rol rol = new Rol(1, "Tester");
        when(rolRepository.save(rol)).thenReturn(rol);
        Rol saved = rolService.saveRol(rol);
        assertNotNull(saved);
        assertEquals("Tester", saved.getNombre());
    }

    @Test 
    public void testDeleteById() {
        Integer id = 1;
        doNothing().when(rolRepository).deleteById(id);
        rolService.deleteRol    (id);
        verify(rolRepository, times(1)).deleteById(id);
    }

}
