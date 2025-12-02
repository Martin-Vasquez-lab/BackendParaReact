package com.techgear.catalogo.controller;

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

import com.techgear.catalogo.model.Carro;
import com.techgear.catalogo.service.CarroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carro")
@Tag(name = "Carros",description = "Operaciones CRUD de carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    @Operation(summary="Obtener todos los carros",description = "Obtiene una lista de todos los carros")
    @ApiResponse(responseCode = "200", description = "Listado de carros",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Carro.class)))
    )
    public ResponseEntity<List<Carro>> obtenerCarros(){
        try {
            List<Carro>carros = carroService.getCarros();
            if (carros.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(carros);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/detalles")
    @Operation(summary="Obtener todos los carros detallados",description = "Obtiene una lista de todos los carros y detalles del usuario")
    @ApiResponse(responseCode = "200", description = "Listado de carros detallado",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Carro.class)))
    )
    public ResponseEntity<?> obtenerCarrosDetalles(){
        try {
            List<Carro> carros = carroService.getCarrosDetalles();
            if (carros.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(carros);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener carro",description = "Obtiene un carro mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carro encontrado", 
            content = @Content(schema = @Schema(implementation = Carro.class))),
        @ApiResponse(responseCode = "404", description = "Carro no encontrado")
    })
    public ResponseEntity<Carro> obtenerCarro(@PathVariable("id")int id){
        try {
            Carro carro = carroService.getCarro(id);
            if (carro==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(carro);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary="Ingresar carro",description = "Ingresa carro a la BD utilizando un JSON")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Carro creado", 
        content = @Content(schema = @Schema(implementation = Carro.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Carro> insertarCarro(@RequestBody Carro carro){
        try {
            Carro newCarro = carroService.saveCarro(carro);
            if (newCarro==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(newCarro);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(summary="Actualizar carro",description = "Actualiza los datos de un carro ya registrado")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Carro actualizado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Carro.class))),
    @ApiResponse(responseCode = "404", description = "Carro no encontrado")
    })
    public ResponseEntity<Carro> actualizarCarro(@RequestBody Carro carro){
        try {
            Carro updCarro = carroService.updateCarro(carro);
            if (updCarro==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updCarro);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar carro",description = "Elimina un carro mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Carro eliminado"),
        @ApiResponse(responseCode = "404", description = "Carro no encontrado")
    })
    public ResponseEntity<Void> eliminarCarro(@PathVariable("id")Integer id){
        try {
            carroService.deleteCarro(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
