package com.tpi.microservicioflota.controller;

import com.tpi.microservicioflota.entity.Camion;
import com.tpi.microservicioflota.entity.Transportista;
import com.tpi.microservicioflota.repository.CamionRepository;
import com.tpi.microservicioflota.repository.TransportistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/flota")
public class FlotaController {

    private final CamionRepository camionRepository;
    private final TransportistaRepository transportistaRepository;

    // EL CONSTRUCTOR AHORA SOLO INYECTA LOS REPOSITORIOS QUE SÍ GESTIONA
    public FlotaController(CamionRepository camionRepository,
                           TransportistaRepository transportistaRepository) {
        this.camionRepository = camionRepository;
        this.transportistaRepository = transportistaRepository;
    }

    // --- Endpoints Protegidos ---

    /**
     * [ADMIN] Endpoint para registrar un nuevo transportista.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transportistas")
    public ResponseEntity<Transportista> registrarTransportista(@RequestBody Transportista transportista) {
        Transportista nuevoTransportista = transportistaRepository.save(transportista);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTransportista);
    }

    /**
     * [ADMIN] Endpoint para registrar un nuevo camión.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/camiones")
    public ResponseEntity<Camion> registrarCamion(@RequestBody Camion camion) {
        Camion nuevoCamion = camionRepository.save(camion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCamion);
    }

    /**
     * [ADMIN] Endpoint para consultar camiones libres.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/camiones/disponibles")
    public ResponseEntity<List<Camion>> obtenerCamionesDisponibles() {
        List<Camion> camionesDisponibles = camionRepository.findByDisponibleTrue();
        return ResponseEntity.ok(camionesDisponibles);
    }

    
}