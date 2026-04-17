package plataforma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import plataforma.domain.Evento;
import plataforma.service.EventoService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/eventos")
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Listar todos los eventos
    @GetMapping
    public String listar(Model model) {
        List<Evento> eventos = eventoService.listarEventos();
        model.addAttribute("eventos", eventos);
        return "eventos/listar";
    }

    // Ver detalle de evento
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Evento> evento = eventoService.obtenerEvento(id);
        if (evento.isPresent()) {
            model.addAttribute("evento", evento.get());
            return "eventos/detalle";
        }
        return "redirect:/eventos";
    }

    // Formulario para crear evento
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("evento", new Evento());
        return "eventos/formulario";
    }

    // Guardar nuevo evento
    @PostMapping
    public String guardar(@ModelAttribute Evento evento) {
        evento.setActivo(true);
        evento.setFechaCreacion(LocalDateTime.now());
        eventoService.crearEvento(evento);
        return "redirect:/eventos";
    }

    // Formulario para editar evento
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Evento> evento = eventoService.obtenerEvento(id);
        if (evento.isPresent()) {
            model.addAttribute("evento", evento.get());
            return "eventos/formulario";
        }
        return "redirect:/eventos";
    }

    // Guardar cambios del evento
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Evento evento) {
        eventoService.editarEvento(id, evento);
        return "redirect:/eventos";
    }

    // Eliminar evento
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return "redirect:/eventos";
    }
}