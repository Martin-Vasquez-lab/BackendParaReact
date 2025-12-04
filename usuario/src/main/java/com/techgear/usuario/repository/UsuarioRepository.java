package com.techgear.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techgear.usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // ✅ AGREGAR ESTE MÉTODO
    Usuario findByCorreo(String correo);
}