package com.techgear.usuario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techgear.usuario.model.Usuario;
import com.techgear.usuario.repository.UsuarioRepository;
import java.util.Map;  // ✅ AÑADE ESTA LÍNEA
import java.util.HashMap;  // ✅ Opcional, pero útil

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔐 MÉTODO DE AUTENTICACIÓN
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        
        if (usuario != null) {
            // USAR REFLEXIÓN PARA ACCEDER A CAMPOS SIN GETTERS
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

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
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