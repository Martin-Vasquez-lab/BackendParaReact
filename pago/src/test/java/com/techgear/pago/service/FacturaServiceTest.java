package com.techgear.pago.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.techgear.pago.model.Factura;
import com.techgear.pago.model.Fpago;
import com.techgear.pago.repository.FacturaRepository;

import java.util.List;
import java.util.Optional;

public class FacturaServiceTest {

    @InjectMocks  // Inyecta los mocks en el servicio
    private FacturaService facturaService;


    @MockBean
    private FacturaRepository facturaRepository;
    
    Fpago fpago= new Fpago();

    String fechaStr = "1969-12-31";
    java.sql.Date fechaSql = java.sql.Date.valueOf(fechaStr);
    
    @Test
    public void testFindAll() {
        when(facturaRepository.findAll()).thenReturn(List.of(new Factura(1, 1, 1, fpago, fechaSql, 1, null, null)));
        List<Factura> facturas = facturaRepository.findAll();
        assertNotNull(facturas);
        assertEquals(1, facturas.size());
    }

    @Test
    public void testFindById() {
        int id = 1;
        Factura factura = new Factura(1, 1, 1, fpago, fechaSql, 1, null, null);
        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));
        Factura found = facturaService.getFactura(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Factura factura = new Factura(1, 1, 1, fpago, fechaSql, 1, null, null);
        when(facturaRepository.save(factura)).thenReturn(factura);
        Factura saved = facturaService.saveFactura(factura);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test 
    public void testDeleteByCodigo() {
        Integer id = 1;
        doNothing().when(facturaRepository).deleteById(id);
        facturaService.deleteFactura(id);
        verify(facturaRepository, times(1)).deleteById(id);
    }

}
