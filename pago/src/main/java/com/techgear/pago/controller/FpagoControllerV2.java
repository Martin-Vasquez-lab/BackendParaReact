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

import com.techgear.pago.assemblers.FpagoModelAssembler;
import com.techgear.pago.model.Fpago;
import com.techgear.pago.service.FpagoService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/fpago/v2")
public class FpagoControllerV2 {

    @Autowired
    private FpagoService fpagoService;

    @Autowired
    private FpagoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Fpago>> obtenerFpagosV2() {
        List<EntityModel<Fpago>> fpagos = fpagoService.getAllFpagos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(fpagos,
                linkTo(methodOn(FpagoControllerV2.class).obtenerFpagosV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Fpago> obtenerFpagoV2(@PathVariable Integer id) {
        Fpago fpago = fpagoService.getFpago(id);
        return assembler.toModel(fpago);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Fpago>> insertarFpagoV2(@RequestBody Fpago fpago) {
        Fpago newFpago = fpagoService.saveFpago(fpago);
        return ResponseEntity
                .created(linkTo(methodOn(FpagoControllerV2.class).obtenerFpagoV2(newFpago.getId())).toUri())
                .body(assembler.toModel(newFpago));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Fpago>> actualizarFpagoV2(@RequestBody Fpago fpago) {
        Fpago updFpago = fpagoService.saveFpago(fpago);
        return ResponseEntity
                .ok(assembler.toModel(updFpago));
    }

    @DeleteMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarFpagoV2(@PathVariable Integer id) {
        fpagoService.deleteFpago(id);
        return ResponseEntity.noContent().build();
    }
}
