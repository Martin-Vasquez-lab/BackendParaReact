package com.techgear.catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.techgear.catalogo.controller.ProductoControllerV2;
import com.techgear.catalogo.model.Producto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler {

    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerV2.class).obtenerProductoV2(producto.getId())).withSelfRel(),
            linkTo(methodOn(ProductoControllerV2.class).obtenerProductosV2()).withRel("productos")
        );
    }

}
