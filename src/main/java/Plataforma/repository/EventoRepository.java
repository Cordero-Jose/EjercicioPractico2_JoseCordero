package plataforma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plataforma.domain.Evento;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Buscar eventos por estado (activo/inactivo)
    List<Evento> findByActivo(Boolean activo);
    
    // Buscar eventos por rango de fechas
    List<Evento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar eventos por coincidencia parcial en nombre (ignorando mayúsculas/minúsculas)
    List<Evento> findByNombreContainingIgnoreCase(String nombre);
}