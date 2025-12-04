package com.techgear.catalogo.controller;

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
import org.springframework.web.bind.annotation.RequestBody;

import com.techgear.catalogo.assemblers.CarroModelAssembler;
import com.techgear.catalogo.model.Carro;
import com.techgear.catalogo.service.CarroService;


@RestController
@RequestMapping("/carro/v2")
public class CarroControllerV2 {

    @Autowired
    private CarroService carroService;

    @Autowired
    private CarroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Carro>> obtenerCarrosV2() {
        List<EntityModel<Carro>> carros = carroService.getCarros().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(carros,
                linkTo(methodOn(CarroControllerV2.class).obtenerCarrosV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Carro> obtenerCarroV2(@PathVariable Integer id) {
        Carro carro = carroService.getCarro(id);
        return assembler.toModel(carro);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Carro>> insertarCarroV2(@RequestBody Carro carro) {
        Carro newCarro = carroService.saveCarro(carro);
        return ResponseEntity
                .created(linkTo(methodOn(CarroControllerV2.class).obtenerCarroV2(newCarro.getId())).toUri())
                .body(assembler.toModel(newCarro));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Carro>> actualizarCarroV2(@RequestBody Carro carro) {
        Carro updCarro = carroService.saveCarro(carro);
        return ResponseEntity
                .ok(assembler.toModel(updCarro));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarCarroV2(@PathVariable Integer id) {
        carroService.deleteCarro(id);
        return ResponseEntity.noContent().build();
    }

}
