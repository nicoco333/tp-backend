package com.tpi.microservicioseguimiento.entity;

import com.tpi.microservicioseguimiento.entity.enums.EstadoTramo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cambios_estado")
public class CambioEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha y hora exacta en que ocurrió la transición (obligatorio para trazabilidad)
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // El nuevo estado al que se transicionó
    @Enumerated(EnumType.STRING) // Guarda el nombre del ENUM como String
    @Column(name = "nuevo_estado", nullable = false)
    private EstadoTramo nuevoEstado;

    // Clave foránea al Tramo al que aplica este cambio de estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tramo_id", nullable = false)
    private Tramo tramo; 

    // 1. Constructor vacío (requerido por JPA)
    public CambioEstado() {
        // Inicializamos la fecha al momento de la creación para registrar el cambio
        this.fechaHora = LocalDateTime.now(); 
    }

    // 2. Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoTramo getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(EstadoTramo nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public Tramo getTramo() {
        return tramo;
    }

    public void setTramo(Tramo tramo) {
        this.tramo = tramo;
    }
}