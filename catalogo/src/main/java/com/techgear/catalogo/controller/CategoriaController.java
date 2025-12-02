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

import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/categoria")

public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary="Obtener todas las categorias",description = "Obtiene una lista de todas las categorias")
    @ApiResponse(responseCode = "200", description = "Listado de categorias",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))
    )
    public ResponseEntity<List<Categoria>> obtenerCategorias(){
        try {
            List<Categoria>categorias = categoriaService.getCategorias();
            if (categorias.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener categoria",description = "Obtiene una categoria mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada", 
            content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable("id")int id){
        try {
            Categoria categoria = categoriaService.getCategoria(id);
            if (categoria==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary="Ingresar categoria",description = "Ingresa categoria a la BD utilizando un JSON")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Categoria creado", 
        content = @Content(schema = @Schema(implementation = Categoria.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Categoria> insertarCategoria(@RequestBody Categoria categoria){
        try {
            Categoria newCategoria = categoriaService.saveCategoria(categoria);
            if (newCategoria==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(newCategoria);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(summary="Actualizar categoria",description = "Actualiza los datos de un categoria ya registrado")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Categoria actualizado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Categoria.class))),
    @ApiResponse(responseCode = "404", description = "Categoria no encontrado")
    })
    public ResponseEntity<Categoria> actualizarCategoria(@RequestBody Categoria categoria){
        try {
            Categoria updCategoria = categoriaService.updateCategoria(categoria);
            if (updCategoria==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updCategoria);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar categoria",description = "Elimina una categoria mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoria eliminado"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrado")
    })
    public ResponseEntity<Void> eliminarCategoria(@PathVariable("id")Integer id){
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
