package com.techgear.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techgear.usuario.model.Rol;
import com.techgear.usuario.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> getAllRoles(){
        return rolRepository.findAll();
    }

    public Rol getRol(int id){
        return rolRepository.findById(id).orElse(null);
    }

    public Rol saveRol(Rol rol){
        return rolRepository.save(rol);
    }

    public Rol updateRol(Rol rol){
        Rol updRol = getRol(rol.getId());
        if (updRol==null) {
            return null;
        }
        updRol.setNombre(rol.getNombre());

        return rolRepository.save(updRol);
    }

    public void deleteRol(Integer id){
        rolRepository.deleteById(id);
    }   
}