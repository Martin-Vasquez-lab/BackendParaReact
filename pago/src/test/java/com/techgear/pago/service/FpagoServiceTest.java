package com.techgear.pago.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.techgear.pago.model.Fpago;
import com.techgear.pago.repository.FpagoRepository;

import java.util.List;
import java.util.Optional;


public class FpagoServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private FpagoService fpagoService;


    @MockBean
    private FpagoRepository fpagoRepository;
    
    
    @Test
    public void testFindAll() {
        when(fpagoRepository.findAll()).thenReturn(List.of(new Fpago(1, "Test")));
        List<Fpago> fpagos = fpagoRepository.findAll();
        assertNotNull(fpagos);
        assertEquals(1, fpagos.size());
    }

    @Test
    public void testFindById() {
        int id = 1;
        Fpago fpago = new Fpago(1, "Test");
        when(fpagoRepository.findById(id)).thenReturn(Optional.of(fpago));
        Fpago found = fpagoService.getFpago(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Fpago fpago = new Fpago(1, "Test");
        when(fpagoRepository.save(fpago)).thenReturn(fpago);
        Fpago saved = fpagoService.saveFpago(fpago);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test 
    public void testDeleteByCodigo() {
        Integer id = 1;
        doNothing().when(fpagoRepository).deleteById(id);
        fpagoService.deleteFpago(id);
        verify(fpagoRepository, times(1)).deleteById(id);
    }

}
