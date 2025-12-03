package com.techgear.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.techgear.usuario.model.Rol;
import java.util.Optional;  // ✅ Añade esta importación

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{
    
    // ✅ AÑADE ESTE MÉTODO
        Optional<Rol> findByNombre(String nombre);

}
