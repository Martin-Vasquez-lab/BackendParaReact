package com.techgear.catalogo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techgear.catalogo.model.Carro;
import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.repository.CarroRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CarroServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private CarroService carroService;


    @Mock
    private CarroRepository carroRepository;
    
    Producto prodT = new Producto();
    
    @Test
    public void testFindAll() {
        when(carroRepository.findAll()).thenReturn(List.of(new Carro(1, prodT,5,1,null)));
        List<Carro> carros = carroRepository.findAll();
        assertNotNull(carros);
        assertEquals(1, carros.size());
    }

    @Test
    public void testFindById() {
        int id = 1;
        Carro carro = new Carro(1, prodT,5,1,null);
        when(carroRepository.findById(id)).thenReturn(Optional.of(carro));
        Carro found = carroService.getCarro(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Carro carro = new Carro(1, prodT,5,1,null);
        when(carroRepository.save(carro)).thenReturn(carro);
        Carro saved = carroService.saveCarro(carro);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test 
    public void testDeleteByCodigo() {
        Integer id = 1;
        doNothing().when(carroRepository).deleteById(id);
        carroService.deleteCarro(id);
        verify(carroRepository, times(1)).deleteById(id);
    }
}
