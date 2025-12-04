package com.techgear.catalogo.controller;

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

import com.techgear.catalogo.model.Producto;
import com.techgear.catalogo.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/producto")

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary="Obtener todos los productos",description = "Obtiene una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Listado de productos",
    content = @Content(mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Producto.class)))
    )
    public ResponseEntity<List<Producto>> obtenerProductos(){
        try {
            List<Producto>productos = productoService.getProductos();
            if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    

    @GetMapping("/{id}")
    @Operation(summary="Obtener producto",description = "Obtiene un producto mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado", 
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> obtenerProducto(@PathVariable("id")int id){
        try {
            Producto producto = productoService.getProducto(id);
            if (producto==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary="Ingresar producto",description = "Ingresa producto a la BD utilizando un JSON")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto creado", 
        content = @Content(schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> insertarProducto(@RequestBody Producto producto){
        try {
            Producto newProducto = productoService.saveProducto(producto);
            if (newProducto==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(newProducto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Operation(summary="Actualizar producto",description = "Actualiza los datos de un producto ya registrado")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto actualizado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto){
        try {
            Producto updProducto = productoService.updateProducto(producto);
            if (updProducto==null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updProducto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar producto",description = "Elimina un producto mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id")Integer id){
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/stock")
@Operation(summary="Reducir stock de producto",description = "Reduce el stock de un producto")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock reducido", 
        content = @Content(schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos")
})
public ResponseEntity<Producto> reducirStock(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> data) {
    try {
        Integer quantity = data.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        Producto updatedProducto = productoService.reduceStock(id, quantity);
        if (updatedProducto != null) {
            return ResponseEntity.ok(updatedProducto);
        }
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        if (e.getMessage().contains("Stock insuficiente")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}

    @PutMapping("/{id}/stock-admin")
    @Operation(summary="Actualizar stock de producto",description = "Actualiza el stock a un nuevo valor")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock actualizado",
        content = @Content(schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> actualizarStock(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> data) {
    try {
        Integer newStock = data.get("newStock");
        if (newStock == null || newStock < 0) {
            return ResponseEntity.badRequest().build();
        }
        
        Producto updatedProducto = productoService.updateStock(id, newStock);
        if (updatedProducto != null) {
            return ResponseEntity.ok(updatedProducto);
        }
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}


    @PostMapping("/{id}/reserve")
    @Operation(summary="Reservar stock temporalmente",description = "Reserva stock para un carrito")
    @ApiResponse(responseCode = "200", description = "Stock reservado")
    public ResponseEntity<Void> reservarStock(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> data) {
        try {
            Integer quantity = data.get("quantity");
            boolean reserved = productoService.reserveStock(id, quantity);
            if (reserved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
