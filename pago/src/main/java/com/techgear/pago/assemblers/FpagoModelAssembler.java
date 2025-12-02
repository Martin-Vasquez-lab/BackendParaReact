package com.techgear.pago.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.techgear.pago.controller.FpagoControllerV2;
import com.techgear.pago.model.Fpago;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FpagoModelAssembler {

    public EntityModel<Fpago> toModel(Fpago fpago) {
        return EntityModel.of(fpago,
            linkTo(methodOn(FpagoControllerV2.class).obtenerFpagoV2(fpago.getId())).withSelfRel(),
            linkTo(methodOn(FpagoControllerV2.class).obtenerFpagosV2()).withRel("fpagos")
        );
    }

}
