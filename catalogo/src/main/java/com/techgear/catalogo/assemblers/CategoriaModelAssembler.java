package com.techgear.catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.techgear.catalogo.controller.CategoriaControllerV2;
import com.techgear.catalogo.model.Categoria;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler {

    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
            linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoriaV2(categoria.getId())).withSelfRel(),
            linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoriasV2()).withRel("categorias")
        );
    }

}
