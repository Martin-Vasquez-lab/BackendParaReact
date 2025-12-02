package com.techgear.usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/rol")
@Tag(name = "Roles",description = "Operaciones CRUD de rol")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping()
    @Operation(summary="Obtener todos los roles",description = "Obtiene una lista de todos los roles")
    @ApiResponse(responseCode = "200", description = "Listado de roles",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Rol.class)))
    )
    public ResponseEntity<List<Rol>> obtenerRoles(){
        try {
            List<Rol>roles = rolService.getAllRoles();
            if (roles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener rol",description = "Obtiene un rol mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado", 
            content = @Content(schema = @Schema(implementation = Rol.class))),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Rol> obtenerRol(@PathVariable("id")Integer id){
        try {
            Rol rol = rolService.getRol(id);
            if (rol==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(rol);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    @Operation(summary="Ingresar rol",description = "Ingresa usuario a la BD utilizando un JSON")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Rol creado", 
        content = @Content(schema = @Schema(implementation = Rol.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Rol> insertarRol(@RequestBody Rol rol){
        try {
            Rol newRol = rolService.saveRol(rol);
            return ResponseEntity.ok(newRol);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping()
    @Operation(summary="Actualizar rol",description = "Actualiza los datos de un rol ya registrado")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Rol actualizado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Rol.class))),
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Rol> actualizarRol(@RequestBody Rol rol){
        try {
            Rol updRol = rolService.updateRol(rol);
            if (updRol==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updRol);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar rol",description = "Elimina un rol mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Rol eliminado"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Void> eliminarRol(@PathVariable("id")Integer id){
        try {
            rolService.deleteRol(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}