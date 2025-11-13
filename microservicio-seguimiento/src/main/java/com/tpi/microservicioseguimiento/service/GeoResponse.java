package com.tpi.microservicioseguimiento.service;

/**
 * DTO para transportar los datos de respuesta del cálculo de distancia de la API externa.
 * Basado en la estructura simplificada del PDF guía.
 */
public class GeoResponse {

    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;

    // Constructor vacío (requerido por Jackson para mapear el JSON)
    public GeoResponse() {
    }

    // Constructor completo para crear la instancia desde GeoService
    public GeoResponse(String origen, String destino, double kilometros, String duracionTexto) {
        this.origen = origen;
        this.destino = destino;
        this.kilometros = kilometros;
        this.duracionTexto = duracionTexto;
    }

    // Getters y Setters (Necesarios para que Jackson y la lógica interna funcionen)

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getKilometros() {
        return kilometros;
    }

    public void setKilometros(double kilometros) {
        this.kilometros = kilometros;
    }

    public String getDuracionTexto() {
        return duracionTexto;
    }

    public void setDuracionTexto(String duracionTexto) {
        this.duracionTexto = duracionTexto;
    }
}