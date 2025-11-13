package com.tpi.microservicioseguimiento.repository;

import com.tpi.microservicioseguimiento.entity.CambioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// La interfaz debe extender de JpaRepository, especificando la Entidad (CambioEstado) 
// y el tipo de dato de su clave primaria (Long).
@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    
    // Al extender JpaRepository, esta interfaz hereda automáticamente:
    // - save(entity)
    // - findById(id)
    // - findAll()
    // - delete(entity)
    // ... y más, lo cual cumple con el principio de Repositorios Automáticos de Spring Data.
    
    // Opcional: Puedes agregar aquí consultas derivadas si necesitas buscar el historial
    // de un tramo específico, por ejemplo:
    // List<CambioEstado> findByTramoIdOrderByFechaHoraAsc(Long tramoId);
}