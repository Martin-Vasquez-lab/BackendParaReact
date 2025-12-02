package com.techgear.pago.model;

import java.sql.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private int usuarioId;

    @Column(name = "carro_id", nullable = false)
    private int carroId;

    @ManyToOne
    @JoinColumn(name = "fpago_id", nullable = false)
    private Fpago formaPago;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private double monto;

    @jakarta.persistence.Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> usuarioDetalles;

    @jakarta.persistence.Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> carroDetalles;
    
}
