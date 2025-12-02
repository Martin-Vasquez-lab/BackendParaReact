package com.techgear.pago.controller;

import java.util.List;
import java.util.Map;

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
import com.techgear.pago.service.FacturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/factura")

public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping()
    @Operation(summary="Obtener todas las facturas",description = "Obtiene una lista de todas las facturass")
    @ApiResponse(responseCode = "200", description = "Listado de roles",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Factura.class)))
    )
    public ResponseEntity<List<Factura>> obtenerFacturas(){
        try {
            List<Factura>facturas = facturaService.getAllFacturas();
            if (facturas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/detalles")
    @Operation(summary="Obtener todos las facturas detalladas",description = "Obtiene una lista de todas las facturass detalladamente")
    @ApiResponse(responseCode = "200", description = "Listado de facturas detallado",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Factura.class)))
    )
    public ResponseEntity<?> obtenerCarrosDetalles(){
        try {
            List<Factura> facturas = facturaService.getFacturasDetalles();
            if (facturas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener factura",description = "Obtiene una factura mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrado", 
            content = @Content(schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrado")
    })
    public ResponseEntity<Factura> obtenerFactura(@PathVariable("id")int id){
        try {
            Factura factura = facturaService.getFactura(id);
            if (factura==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    @Operation(summary="Crear factura con validación de stock",description = "Crea factura validando stock disponible")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Factura creada", 
        content = @Content(schema = @Schema(implementation = Factura.class))),
    @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos")
    })
    public ResponseEntity<Factura> insertarFactura(@RequestBody Factura factura){
        try {
            Factura newFactura = facturaService.processPaymentWithStockValidation(factura);
            return ResponseEntity.ok(newFactura);
        } catch (Exception e) {
            // Manejar específicamente la excepción de stock insuficiente
            if (e.getMessage().contains("Stock insuficiente")) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping()
    @Operation(summary="Actualizar factura",description = "Actualiza los datos de una factura ya registrada")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Factura actualizada",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Factura.class))),
    @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<Factura> actualizarFactura(@RequestBody Factura factura){
        try {
            Factura updFactura = facturaService.updateFactura(factura);
            if (updFactura==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updFactura);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar factura",description = "Elimina una factura mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Factura eliminado"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrado")
    })
    public ResponseEntity<Void> eliminarFactura(@PathVariable("id")Integer id){
        try {
            facturaService.deleteFactura(id);
            return ResponseEntity.noContent().build();
           
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
