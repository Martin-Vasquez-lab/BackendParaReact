package com.techgear.pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techgear.pago.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura,Integer>{

}
