package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Tarifa;
import com.tpi.microservicioseguimiento.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    
    // Inyección de dependencia del repositorio
    @Autowired 
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    // Método que recibe la lógica de persistencia del controlador
    public Tarifa registrarTarifa(Tarifa nuevaTarifa) {
        // En el futuro, aquí irían validaciones de negocio sobre la Tarifa
        return tarifaRepository.save(nuevaTarifa);
    }
    
    // Aquí implementaremos el cálculo de costos más adelante
}