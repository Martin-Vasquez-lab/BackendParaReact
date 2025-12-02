package com.techgear.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techgear.catalogo.model.Carro;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Integer>{

}
