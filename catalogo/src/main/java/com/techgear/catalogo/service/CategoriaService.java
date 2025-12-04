package com.techgear.catalogo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techgear.catalogo.model.Categoria;
import com.techgear.catalogo.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }

    public Categoria getCategoria(int id){
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria saveCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public Categoria updateCategoria(Categoria categoria){
        Categoria updcategoria = getCategoria(categoria.getId());
        if (updcategoria==null) {
            return null;
        }
        updcategoria.setNombre(categoria.getNombre());
        updcategoria.setDescripcion(categoria.getDescripcion());
        
        return categoriaRepository.save(updcategoria);
    }

    public void deleteCategoria(Integer id){
        categoriaRepository.deleteById(id);
    }   
}
