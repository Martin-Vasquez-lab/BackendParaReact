package com.techgear.pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techgear.pago.model.Fpago;

@Repository
public interface FpagoRepository extends JpaRepository<Fpago,Integer>{

}
