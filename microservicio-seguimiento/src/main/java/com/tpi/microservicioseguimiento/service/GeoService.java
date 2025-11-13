package com.tpi.microservicioseguimiento.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClientResponseException; // Para manejo de errores HTTP

@Service
public class GeoService {

    // [1] URL base de la API de Google Maps Distance Matrix (constante del PDF guía)
    private static final String GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/maps/api"; 

    // [2] Inyección de la API Key configurada en application.properties
    @Value("${google.maps.apikey}")
    private String apiKey;

    private final RestClient restClient;

    // Constructor que inicializa el RestClient y usa la URL base
    public GeoService() {
        // Inicialización del RestClient con la URL base
        this.restClient = RestClient.builder().baseUrl(GOOGLE_MAPS_BASE_URL).build();
    }

    /**
     * Calcula la distancia y duración entre dos puntos (lat,long) usando la API de Google Maps.
     * @param origen Coordenadas del origen (ej: "-31.4167,-64.183")
     * @param destino Coordenadas del destino (ej: "-32.890,-68.827")
     * @return GeoResponse con distancia en KM y tiempo estimado.
     */
    public GeoResponse calcularDistancia(String origen, String destino) throws Exception {
        
        // Estructura de la URI para el endpoint Distance Matrix
        String uri = "/distancematrix/json?origins=" + origen + 
                     "&destinations=" + destino + 
                     "&units=metric&key=" + apiKey;

        String responseBody;
        try {
            // [3] Ejecutar la petición síncrona con RestClient
            responseBody = restClient.get()
                                     .uri(uri)
                                     .retrieve()
                                     .body(String.class); // Se obtiene el cuerpo como String para parsear
        } catch (RestClientResponseException ex) {
             // Manejo de errores de conexión o HTTP (ej. 400, 500)
             throw new Exception("Error al conectar con la API de Google Maps: Código " + ex.getStatusCode().value(), ex);
        }

        // [4] Procesamiento y Extracción del JSON (usando Jackson)
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        
        // Navegamos a la estructura donde están los datos: rows[0].elements[0]
        JsonNode element = root.path("rows").get(0).path("elements").get(0);

        // Verificamos el estado de la respuesta del cálculo (no es un error HTTP, sino un error de cálculo)
        String status = element.path("status").asText();
        if (!"OK".equals(status)) {
            // Si el estado es NOT_FOUND, ZERO_RESULTS, etc.
            throw new Exception("Ruta no válida o no encontrada por la API externa. Estado: " + status);
        }

        // [5] Extracción y Conversión de valores
        double distanciaMetros = element.path("distance").path("value").asDouble();
        String duracionTexto = element.path("duration").path("text").asText();
        
        // Mapeo al DTO (convertimos metros a kilómetros)
        return new GeoResponse(
            origen, 
            destino,
            distanciaMetros / 1000.0, // Conversión obligatoria a KM
            duracionTexto
        );
    }
}