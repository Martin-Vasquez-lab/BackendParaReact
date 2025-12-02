package com.techgear.usuario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.techgear.usuario.controller.RolControllerV2;
import com.techgear.usuario.model.Rol;

@Component
public class RolModelAssembler {

    public EntityModel<Rol> toModel(Rol rol) {
        return EntityModel.of(rol,
            linkTo(methodOn(RolControllerV2.class).obtenerRolV2(rol.getId())).withSelfRel(),
            linkTo(methodOn(RolControllerV2.class).obtenerRolesV2()).withRel("roles")
        );
    }

}
