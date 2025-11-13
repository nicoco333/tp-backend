package com.tpi.microservicioseguimiento.controller;

import com.tpi.microservicioseguimiento.service.GeoResponse;
import com.tpi.microservicioseguimiento.service.GeoService;
import org.springframework.web.bind.annotation.*;

// Importaciones para Swagger (Documentación)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
// Ruta base para este controlador de utilidad
@RequestMapping("/api/distancia") 
@Tag(name = "Utilidades", description = "Endpoints de utilidad para cálculos externos (API Google Maps)")
public class GeoController {

    private final GeoService geoService;

    // Constructor Manual para Inyección de Dependencias (Reemplaza a @RequiredArgsConstructor)
    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    /**
     * Endpoint de prueba para verificar la integración con la API de Google Maps (Requisito Obligatorio).
     * GET http://localhost:8083/api/distancia?origen=-31.4167,-64.183&destino=-32.890,-68.827
     */
    @Operation(summary = "Calcula la distancia y duración entre dos coordenadas",
               description = "Utiliza Google Maps Distance Matrix API para obtener kilómetros y tiempo de viaje.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distancia calculada con éxito"),
            @ApiResponse(responseCode = "500", description = "Error al conectar o procesar la respuesta de la API externa"),
            @ApiResponse(responseCode = "400", description = "Coordenadas o parámetros inválidos")
    })
    @GetMapping 
    public GeoResponse obtenerDistancia(
        // @RequestParam mapea los parámetros de la URL (query string)
        @RequestParam String origen,
        @RequestParam String destino) throws Exception {
        
        // El controlador solo delega la tarea al servicio
        return geoService.calcularDistancia(origen, destino);
    }
}