package com.techgear.pago.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.techgear.pago.controller.FacturaControllerV2;
import com.techgear.pago.model.Factura;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FacturaModelAssembler {

    public EntityModel<Factura> toModel(Factura factura) {
        return EntityModel.of(factura,
            linkTo(methodOn(FacturaControllerV2.class).obtenerFacturaV2(factura.getId())).withSelfRel(),
            linkTo(methodOn(FacturaControllerV2.class).obtenerFacturasV2()).withRel("facturas")
        );
    }

}
    