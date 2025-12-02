package com.techgear.pago.controller;

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

import com.techgear.pago.model.Factura;
import com.techgear.pago.model.Fpago;
import com.techgear.pago.service.FpagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/fpago")

public class FpagoController {

    @Autowired
    private FpagoService fpagoService;

    @GetMapping()
    @Operation(summary="Obtener todos los fpagos",description = "Obtiene una lista de todos los fpagos")
    @ApiResponse(responseCode = "200", description = "Listado de fpagos",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Factura.class)))
    )
    public ResponseEntity<List<Fpago>> obtenerFpagos(){
        try {
            List<Fpago>fpagos = fpagoService.getAllFpagos();
            if (fpagos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(fpagos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener fpago",description = "Obtiene un fpago mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fpago encontrado", 
            content = @Content(schema = @Schema(implementation = Fpago.class))),
        @ApiResponse(responseCode = "404", description = "Fpago no encontrado")
    })
    public ResponseEntity<Fpago> obtenerUsuario(@PathVariable("id")int id){
        try {
            Fpago fpago = fpagoService.getFpago(id);
            if (fpago==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(fpago);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    @Operation(summary="Ingresar fpago",description = "Ingresa fpago a la BD utilizando un JSON")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Fpago creado", 
        content = @Content(schema = @Schema(implementation = Fpago.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Fpago> insertarUsuario(@RequestBody Fpago fpago){
        try {
            Fpago newFpago = fpagoService.saveFpago(fpago);
            return ResponseEntity.ok(newFpago);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping()
    @Operation(summary="Actualizar fpago",description = "Actualiza los datos de un fpago ya registrado")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Fpago actualizado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Fpago.class))),
    @ApiResponse(responseCode = "404", description = "Fpago no encontrado")
    })
    public ResponseEntity<Fpago> actualizarUsuario(@RequestBody Fpago fpago){
        try {
            Fpago updFpago = fpagoService.updateFpago(fpago);
            if (updFpago==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updFpago);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar fpago",description = "Elimina un fpago mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Fpago eliminado"),
        @ApiResponse(responseCode = "404", description = "Fpago no encontrado")
    })
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("id")Integer id){
        try {
            fpagoService.deleteFpago(id);
                return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
