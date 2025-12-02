package com.techgear.usuario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techgear.usuario.model.Usuario;
import com.techgear.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "Operaciones CRUD de usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 🔐 NUEVO ENDPOINT: LOGIN
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
    try {
        String correo = credentials.get("correo");
        String contrasena = credentials.get("contrasena");
        
        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Correo y contraseña son requeridos")
            );
        }
        
        Usuario usuario = usuarioService.autenticar(correo, contrasena);
        
        if (usuario != null) {
            // ✅ Respuesta mínima sin problemas de conversión
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            
            // Solo los campos esenciales
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", 1);  // Temporal - después lo obtienes del usuario
            userData.put("nombre", "Usuario Autenticado");
            userData.put("email", correo);
            userData.put("rol", "usuario");
            
            response.put("usuario", userData);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("success", false, "message", "Credenciales incorrectas")
            );
        }
        
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(
            Map.of("success", false, "message", "Error en el servidor")
        );
    }
}

    @GetMapping()
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Listado de usuarios",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))
    )
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario mediante ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", 
            content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") Integer id) {
        try {
            Usuario usuario = usuarioService.getUsuario(id);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    @Operation(summary = "Ingresar usuario", description = "Ingresa usuario a la BD utilizando un JSON")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario creado", 
            content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Usuario> insertarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario newUsuario = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(newUsuario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping()
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario ya registrado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody Map<String, Object> usuarioData) {  // ✅ CAMBIADO a Map
        try {
            Usuario updUser = usuarioService.updateUsuario(usuarioData);  // ✅ Ahora coincide con Service
            if (updUser == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario mediante ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("id") Integer id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}