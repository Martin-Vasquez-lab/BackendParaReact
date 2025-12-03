package com.techgear.usuario.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techgear.usuario.model.Usuario;
import com.techgear.usuario.model.Rol;
import com.techgear.usuario.repository.UsuarioRepository;
import com.techgear.usuario.repository.RolRepository;
import jakarta.annotation.PostConstruct;  // Para el método de verificación

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired  // ✅ NUEVO - Agrega esta línea
    private RolRepository rolRepository;

    // 🆕 MÉTODO TEMPORAL PARA VERIFICAR ROLES
    @PostConstruct
    public void verificarRoles() {
        System.out.println("=== VERIFICANDO ROLES EN BD ===");
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            System.out.println("⚠️  No hay roles en la BD!");
        } else {
            System.out.println("📋 Roles encontrados:");
            roles.forEach(r -> System.out.println("   ID: " + r.getId() + " | Nombre: '" + r.getNombre() + "'"));
        }
        System.out.println("==============================");
    }

    // 🆕 MÉTODO PARA REGISTRAR USUARIO CON ASIGNACIÓN AUTOMÁTICA DE ROL
    public Usuario registrarUsuario(Usuario usuario) {
        try {
            System.out.println("📧 Registrando usuario con email: " + usuario.getCorreo());
            
            // 1. Determinar rol según email
            String nombreRol;
            if (usuario.getCorreo().contains("@admin.") || 
                usuario.getCorreo().startsWith("admin@")) {
                nombreRol = "admin";  // ← minúscula como en tu BD
            } else {
                nombreRol = "Usuario";  // ← con 'U' mayúscula como en tu BD
            }
            
            System.out.println("🎯 Buscando rol: '" + nombreRol + "'");
            
            // 2. Buscar rol en BD
            Optional<Rol> rolOpt = rolRepository.findByNombre(nombreRol);
            
            if (rolOpt.isEmpty()) {
                System.out.println("❌ Error: Rol '" + nombreRol + "' no encontrado en BD");
                // Listar roles disponibles para debug
                List<Rol> todos = rolRepository.findAll();
                System.out.println("📋 Roles disponibles:");
                todos.forEach(r -> System.out.println("   - " + r.getNombre()));
                throw new RuntimeException("Rol no encontrado: " + nombreRol);
            }
            
            Rol rol = rolOpt.get();
            System.out.println("✅ Rol encontrado: ID=" + rol.getId() + ", Nombre=" + rol.getNombre());
            
            // 3. Asignar rol al usuario
            usuario.setRol(rol);
            
            // 4. Guardar usuario
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("👍 Usuario registrado con ID: " + usuarioGuardado.getId());
            
            return usuarioGuardado;
            
        } catch (Exception e) {
            System.out.println("💥 Error en registro: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Re-lanza la excepción para que el controller la maneje
        }
    }

    // 🔐 MÉTODO DE AUTENTICACIÓN (existente)
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        
        if (usuario != null) {
            try {
                java.lang.reflect.Field contrasenaField = Usuario.class.getDeclaredField("contrasena");
                contrasenaField.setAccessible(true);
                String contrasenaDB = (String) contrasenaField.get(usuario);
                
                if (contrasenaDB.equals(contrasena)) {
                    return usuario;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 📋 MÉTODOS EXISTENTES
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // 🆕 MODIFICADO: Ahora usa registro con rol
    public Usuario saveUsuario(Usuario usuario) {
        return registrarUsuario(usuario);  // Usa el nuevo método que asigna rol
    }

    // 🛠️ MÉTODO ACTUALIZADO SIN DEPENDER DE GETTERS
    public Usuario updateUsuario(Map<String, Object> userData) {
        try {
            if (userData == null || !userData.containsKey("id")) {
                return null;
            }
            
            Integer id = ((Number) userData.get("id")).intValue();
            Usuario updUser = getUsuario(id);
            
            if (updUser == null) {
                return null;
            }
            
            // Actualizar campos usando reflexión
            if (userData.containsKey("nombre")) {
                java.lang.reflect.Field nombreField = Usuario.class.getDeclaredField("nombre");
                nombreField.setAccessible(true);
                nombreField.set(updUser, userData.get("nombre"));
            }
            
            if (userData.containsKey("contrasena")) {
                java.lang.reflect.Field contrasenaField = Usuario.class.getDeclaredField("contrasena");
                contrasenaField.setAccessible(true);
                contrasenaField.set(updUser, userData.get("contrasena"));
            }
            
            if (userData.containsKey("correo")) {
                java.lang.reflect.Field correoField = Usuario.class.getDeclaredField("correo");
                correoField.setAccessible(true);
                correoField.set(updUser, userData.get("correo"));
            }
            
            if (userData.containsKey("telefono")) {
                java.lang.reflect.Field telefonoField = Usuario.class.getDeclaredField("telefono");
                telefonoField.setAccessible(true);
                telefonoField.set(updUser, userData.get("telefono"));
            }
            
            return usuarioRepository.save(updUser);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}