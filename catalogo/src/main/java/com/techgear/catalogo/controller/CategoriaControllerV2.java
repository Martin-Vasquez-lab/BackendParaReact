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

import com.techgear.catalogo.assemblers.CategoriaModelAssembler;
import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.service.CategoriaService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/categoria/v2")
public class CategoriaControllerV2 {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Categoria>> obtenerCategoriasV2() {
        List<EntityModel<Categoria>> categorias = categoriaService.getCategorias().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoriasV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Categoria> obtenerCategoriaV2(@PathVariable Integer id) {
        Categoria categoria = categoriaService.getCategoria(id);
        return assembler.toModel(categoria);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Categoria>> insertarCategoriaV2(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaService.saveCategoria(categoria);
        return ResponseEntity
                .created(linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoriaV2(newCategoria.getId())).toUri())
                .body(assembler.toModel(newCategoria));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoriaV2(@RequestBody Categoria categoria) {
        Categoria updCategoria = categoriaService.saveCategoria(categoria);
        return ResponseEntity
                .ok(assembler.toModel(updCategoria));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarCategoriaV2(@PathVariable Integer id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }

}
