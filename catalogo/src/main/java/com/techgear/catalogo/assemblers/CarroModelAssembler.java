package com.techgear.catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.techgear.catalogo.controller.CarroControllerV2;
import com.techgear.catalogo.model.Carro;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarroModelAssembler {

    public EntityModel<Carro> toModel(Carro carro) {
        return EntityModel.of(carro,
            linkTo(methodOn(CarroControllerV2.class).obtenerCarroV2(carro.getId())).withSelfRel(),
            linkTo(methodOn(CarroControllerV2.class).obtenerCarrosV2()).withRel("carros")
        );
    }

}
