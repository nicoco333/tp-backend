package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.entity.Deposito;
import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.service.DepositoService;
import com.tpi.microservicioseguimiento.service.TarifaService;
import com.tpi.microservicioseguimiento.service.RutaService; // Necesario para la lógica de Ruta
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Importaciones de Swagger (asumiendo que ya agregaste las dependencias al pom.xml)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/seguimiento") 
@Tag(name = "Seguimiento y Maestros", description = "Endpoints para Depósitos, Tarifas y Rutas")
public class SeguimientoController {

    // [1] Inyección de dependencias como campos 'final'
    private final DepositoService depositoService;
    private final TarifaService tarifaService;
    private final RutaService rutaService; 
    
    // [2] Constructor Manual para Inyección (Reemplaza a @RequiredArgsConstructor)
    // Spring inyectará automáticamente estos servicios al crear el bean del controlador 
    public SeguimientoController(DepositoService depositoService,
                               TarifaService tarifaService,
                               RutaService rutaService) {
        this.depositoService = depositoService;
        this.tarifaService = tarifaService;
        this.rutaService = rutaService;
    }
    // NOTA: Los repositorios (tarifaRepository, depositoRepository, etc.) se ELIMINAN del controlador.
    
    /**
     * [ADMIN] Endpoint para registrar un nuevo depósito.
     */
    @Operation(summary = "Registra un nuevo depósito", description = "Solo para uso de Administradores/Operadores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Depósito creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. coordenadas nulas)")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/depositos")
    public ResponseEntity<Deposito> registrarDeposito(@RequestBody Deposito deposito) {
        // Delegamos la lógica de persistencia al Service
        Deposito nuevoDeposito = depositoService.registrarDeposito(deposito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDeposito);
    }

    /**
     * [ADMIN] Endpoint para listar todos los depósitos disponibles.
     */
    @Operation(summary = "Lista todos los depósitos")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/depositos")
    public ResponseEntity<List<Deposito>> listarDepositos() {
        // Delegamos la lógica de consulta al Service
        List<Deposito> depositos = depositoService.listarTodos();
        return ResponseEntity.ok(depositos);
    }
    
    /**
     * [ADMIN] Endpoint para registrar una nueva tarifa.
     */
    @Operation(summary = "Registra una nueva tarifa de costos", description = "Solo para uso de Administradores/Operadores")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tarifas")
    public ResponseEntity<Tarifa> registrarTarifa(@RequestBody Tarifa nuevaTarifa) {
        // Delegamos la lógica de persistencia al Service
        Tarifa tarifaGuardada = tarifaService.registrarTarifa(nuevaTarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
    }

    /**
     * [ADMIN] Endpoint para asignar una ruta (con sus tramos) a una solicitud.
     * NOTA: La lógica compleja de validación, cálculo de tramos y asignación se MOVERÁ al Servicio.
     */
    @Operation(summary = "Asigna una ruta y sus tramos a una solicitud", description = "Realiza validaciones de estructura y calcula depósitos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ruta asignada con éxito"),
            @ApiResponse(responseCode = "400", description = "Ruta o tramos inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rutas/asignar")
    public ResponseEntity<Ruta> asignarRuta(@RequestBody Ruta ruta) {
        // Delegamos TODA la lógica de negocio, validación y persistencia al Service
        try {
            Ruta rutaGuardada = rutaService.asignarRuta(ruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(rutaGuardada);
        } catch (IllegalArgumentException e) {
            // Usamos ResponseEntity.badRequest() para errores de cliente
            return ResponseEntity.badRequest().build(); 
        }
    }
    
    // Faltaría agregar aquí los endpoints obligatorios del TPI:
    // - GET /solicitudes/{id}/ruta/tentativa
    // - GET /solicitudes/{id}/seguimiento
    // - PUT /tramos/{id}/iniciar
    // - PUT /tramos/{id}/finalizar
}