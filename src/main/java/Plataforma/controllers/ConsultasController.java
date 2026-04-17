package plataforma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import plataforma.domain.Evento;
import plataforma.domain.Usuario;
import plataforma.domain.Rol;
import plataforma.service.EventoService;
import plataforma.service.UsuarioService;
import plataforma.service.RolService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/consultas")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'ORGANIZADOR')")
public class ConsultasController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // Página principal de consultas
    @GetMapping
    public String index() {
        return "consultas/index";
    }

    // Consulta: Eventos por estado
    @GetMapping("/eventos-por-estado")
    public String eventosPorEstado(@RequestParam(required = false, defaultValue = "true") Boolean activo, Model model) {
        List<Evento> eventos = eventoService.buscarPorEstado(activo);
        model.addAttribute("eventos", eventos);
        model.addAttribute("estado", activo ? "Activos" : "Inactivos");
        return "consultas/eventos-por-estado";
    }

    // Consulta: Eventos por rango de fechas
    @GetMapping("/eventos-por-rango")
    public String eventosPorRango(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            Model model) {
        
        List<Evento> eventos;
        
        if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDate inicio = LocalDate.parse(fechaInicio, formatter);
            LocalDate fin = LocalDate.parse(fechaFin, formatter);
            eventos = eventoService.buscarPorRango(inicio, fin);
        } else {
            eventos = eventoService.listarEventos();
        }
        
        model.addAttribute("eventos", eventos);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "consultas/eventos-por-rango";
    }

    // Consulta: Usuarios por rol
    @GetMapping("/usuarios-por-rol")
    public String usuariosPorRol(@RequestParam(required = false) Long rolId, Model model) {
        List<Usuario> usuarios;
        String rolSeleccionado = "Todos";
        
        if (rolId != null) {
            Optional<Rol> rol = rolService.obtenerRol(rolId);
            if (rol.isPresent()) {
                usuarios = usuarioService.buscarPorRol(rol.get());
                rolSeleccionado = rol.get().getNombre();
            } else {
                usuarios = usuarioService.listarUsuarios();
            }
        } else {
            usuarios = usuarioService.listarUsuarios();
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("rolSeleccionado", rolSeleccionado);
        return "consultas/usuarios-por-rol";
    }

    // Consulta: Eventos por nombre
    @GetMapping("/eventos-por-nombre")
    public String eventosPorNombre(@RequestParam(required = false) String nombre, Model model) {
        List<Evento> eventos;
        
        if (nombre != null && !nombre.isEmpty()) {
            eventos = eventoService.buscarPorNombre(nombre);
        } else {
            eventos = eventoService.listarEventos();
        }
        
        model.addAttribute("eventos", eventos);
        model.addAttribute("nombreBuscado", nombre != null ? nombre : "");
        return "consultas/eventos-por-nombre";
    }

    // Consulta: Estadísticas generales
    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        long totalEventos = eventoService.contarEventos();
        long eventosActivos = eventoService.contarEventosActivos();
        long totalUsuarios = usuarioService.contarUsuarios();
        long usuariosActivos = usuarioService.contarUsuariosActivos();
        
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosActivos", eventosActivos);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", usuariosActivos);
        
        return "consultas/estadisticas";
    }
}