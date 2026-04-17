package plataforma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plataforma.domain.Evento;
import plataforma.repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // Listar todos los eventos
    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    // Obtener un evento por ID
    public Optional<Evento> obtenerEvento(Long id) {
        return eventoRepository.findById(id);
    }

    // Crear un nuevo evento
    public Evento crearEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    // Editar un evento
    public Evento editarEvento(Long id, Evento eventoActualizado) {
        Optional<Evento> eventoExistente = eventoRepository.findById(id);
        if (eventoExistente.isPresent()) {
            Evento evento = eventoExistente.get();
            evento.setNombre(eventoActualizado.getNombre());
            evento.setDescripcion(eventoActualizado.getDescripcion());
            evento.setFecha(eventoActualizado.getFecha());
            evento.setCapacidad(eventoActualizado.getCapacidad());
            evento.setActivo(eventoActualizado.getActivo());
            return eventoRepository.save(evento);
        }
        return null;
    }

    // Eliminar un evento
    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }

    // Buscar eventos por estado (activos/inactivos)
    public List<Evento> buscarPorEstado(Boolean activo) {
        return listarEventos().stream()
                .filter(evento -> evento.getActivo().equals(activo))
                .collect(Collectors.toList());
    }

    // Buscar eventos por rango de fechas
    public List<Evento> buscarPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return listarEventos().stream()
                .filter(evento -> !evento.getFecha().isBefore(fechaInicio) && !evento.getFecha().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    // Buscar eventos por nombre
    public List<Evento> buscarPorNombre(String nombre) {
        return listarEventos().stream()
                .filter(evento -> evento.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Contar eventos activos
    public long contarEventosActivos() {
        return listarEventos().stream()
                .filter(Evento::getActivo)
                .count();
    }

    // Contar total de eventos
    public long contarEventos() {
        return eventoRepository.count();
    }
}