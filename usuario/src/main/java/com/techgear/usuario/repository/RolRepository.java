package com.techgear.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techgear.usuario.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

}
