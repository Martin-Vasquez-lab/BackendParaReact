package com.techgear.catalogo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class CategoriaServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private CategoriaService categoriaService;


    @Mock
    private CategoriaRepository categoriaRepository;
    
    
    @Test
    public void testFindAll() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria(1,"Test","Testing TechGear")));
        List<Categoria> categorias = categoriaRepository.findAll();
        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testFindById() {
        int id = 1;
        Categoria categoria = new Categoria(1, "Test","Testing TechGear");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        Categoria found = categoriaService.getCategoria(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Categoria categoria = new Categoria(1, "Test","Testing TechGear");
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        Categoria saved = categoriaService.saveCategoria(categoria);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test 
    public void testDeleteByCodigo() {
        Integer id = 1;
        doNothing().when(categoriaRepository).deleteById(id);
        categoriaService.deleteCategoria(id);
        verify(categoriaRepository, times(1)).deleteById(id);
    }

}
