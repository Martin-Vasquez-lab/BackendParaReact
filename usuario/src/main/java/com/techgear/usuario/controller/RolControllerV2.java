package com.techgear.usuario.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.techgear.usuario.assemblers.RolModelAssembler;
import com.techgear.usuario.model.Rol;
import com.techgear.usuario.service.RolService;


@RestController
@RequestMapping("/rol/v2")
@Component
public class RolControllerV2 {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Rol>> obtenerRolesV2() {
        List<EntityModel<Rol>> roles = rolService.getAllRoles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(roles,
                linkTo(methodOn(RolControllerV2.class).obtenerRolesV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Rol> obtenerRolV2(@PathVariable Integer id) {
        Rol rol = rolService.getRol(id);
        return assembler.toModel(rol);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> insertarRolV2(@RequestBody Rol rol) {
        Rol newRol = rolService.saveRol(rol);
        return ResponseEntity
                .created(linkTo(methodOn(RolControllerV2.class).obtenerRolV2(newRol.getId())).toUri())
                .body(assembler.toModel(newRol));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> actualizarRolV2(@RequestBody Rol rol) {
        Rol updRol = rolService.saveRol(rol);
        return ResponseEntity
                .ok(assembler.toModel(updRol));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarRolV2(@PathVariable Integer id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }

}
