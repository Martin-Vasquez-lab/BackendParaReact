package com.techgear.usuario.assemblers;

import org.springframework.stereotype.Component;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.techgear.usuario.controller.UsuarioControllerV2;
import com.techgear.usuario.model.Usuario;

@Component
public class UsuarioModelAssembler {

    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerV2.class).obtenerUsuarioV2(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).obtenerUsuariosV2()).withRel("usuarios")
        );
    }
}