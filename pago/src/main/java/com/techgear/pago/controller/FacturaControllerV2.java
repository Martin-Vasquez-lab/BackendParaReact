package com.techgear.pago.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techgear.pago.assemblers.FacturaModelAssembler;
import com.techgear.pago.model.Factura;
import com.techgear.pago.service.FacturaService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/factura/v2")
public class FacturaControllerV2 {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Factura>> obtenerFacturasV2() {
        List<EntityModel<Factura>> facturas = facturaService.getAllFacturas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(facturas,
                linkTo(methodOn(FacturaControllerV2.class).obtenerFacturasV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Factura> obtenerFacturaV2(@PathVariable Integer id) {
        Factura factura = facturaService.getFactura(id);
        return assembler.toModel(factura);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Factura>> insertarFacturaV2(@RequestBody Factura factura) {
        Factura newFactura = facturaService.saveFactura(factura);
        return ResponseEntity
                .created(linkTo(methodOn(FacturaControllerV2.class).obtenerFacturaV2(newFactura.getId())).toUri())
                .body(assembler.toModel(newFactura));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Factura>> actualizarFacturaV2(@RequestBody Factura factura) {
        Factura updFactura = facturaService.saveFactura(factura);
        return ResponseEntity
                .ok(assembler.toModel(updFactura));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarFacturaV2(@PathVariable Integer id) {
        facturaService.deleteFactura(id);
        return ResponseEntity.noContent().build();
    }
}
