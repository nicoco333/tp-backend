package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Ruta;
import com.tpi.microservicioseguimiento.entity.Tramo;
import com.tpi.microservicioseguimiento.entity.enums.EstadoTramo;
import com.tpi.microservicioseguimiento.entity.enums.TipoTramo;
import com.tpi.microservicioseguimiento.repository.RutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio de las Rutas y sus Tramos asociados.
 */
@Service
@RequiredArgsConstructor // Inyecta automáticamente los campos 'final' (RutaRepository)
public class RutaService {

    // Inyección del repositorio (necesario para la persistencia)
    private final RutaRepository rutaRepository;

    /**
     * Asigna y guarda una nueva ruta, realizando los cálculos y validaciones de negocio.
     * Esta lógica fue movida del SeguimientoController.
     * * @param ruta La entidad Ruta recibida con la lista de Tramos.
     * @return La Ruta persistida con todos los campos calculados.
     * @throws IllegalArgumentException Si la ruta no tiene tramos válidos.
     */
    public Ruta asignarRuta(Ruta ruta) throws IllegalArgumentException {
        
        // [1] Validación de Negocio: La ruta debe tener tramos
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            // Se lanza una excepción no chequeada (RuntimeException) para ser manejada 
            // por el Controller (que devuelve el 400 Bad Request)
            throw new IllegalArgumentException("La ruta debe contener al menos un tramo para ser asignada.");
        }

        List<Tramo> tramos = ruta.getTramos();
        
        // [2] Cálculo de Cantidad de Tramos
        ruta.setCantidadTramos(tramos.size());
        
        // [3] Cálculo de Cantidad de Depósitos Intermedios
        long depositosIntermedios = tramos.stream()
                .filter(t -> t.getTipo() == TipoTramo.ORIGEN_DEPOSITO || 
                             t.getTipo() == TipoTramo.DEPOSITO_DEPOSITO)
                .count();
        ruta.setCantidadDepositos((int) depositosIntermedios);

        // [4] Establecer el estado inicial (ASIGNADO)
        for (Tramo tramo : tramos) {
            tramo.setEstado(EstadoTramo.ASIGNADO);
        }

        // [5] Persistencia: Guardar la ruta (la persistencia en cascada guardará los tramos)
        Ruta rutaGuardada = rutaRepository.save(ruta);
        
        return rutaGuardada;
    }
    
    // NOTA: Aquí se agregarán métodos futuros como:
    // - obtenerRutaPorSolicitud(long solicitudId)
    // - actualizarEstadoTramo(long tramoId, EstadoTramo nuevoEstado)
    // - calcularRutaTentativa(origen, destino, depositosIntermedios) -> Esto usará GeoService/TarifaService

}