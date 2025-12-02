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

import com.techgear.catalogo.assemblers.ProductoModelAssembler;
import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.service.ProductoService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/producto/v2")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Producto>> obtenerProductosV2() {
        List<EntityModel<Producto>> productos = productoService.getProductos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).obtenerProductosV2()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Producto> obtenerProductoV2(@PathVariable Integer id) {
        Producto producto = productoService.getProducto(id);
        return assembler.toModel(producto);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> insertarRolV2(@RequestBody Producto producto) {
        Producto newProducto = productoService.saveProducto(producto);
        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerV2.class).obtenerProductoV2(newProducto.getId())).toUri())
                .body(assembler.toModel(newProducto));
    }

    @PutMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> updateCarrera(@RequestBody Producto producto) {
        Producto updRol = productoService.saveProducto(producto);
        return ResponseEntity
                .ok(assembler.toModel(updRol));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarProductoV2(@PathVariable Integer id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

}
