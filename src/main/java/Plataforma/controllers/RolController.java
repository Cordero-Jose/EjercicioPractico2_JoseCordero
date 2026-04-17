package plataforma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import plataforma.domain.Rol;
import plataforma.service.RolService;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RolController {

    @Autowired
    private RolService rolService;

    // Listar todos los roles
    @GetMapping
    public String listar(Model model) {
        List<Rol> roles = rolService.listarRoles();
        model.addAttribute("roles", roles);
        return "roles/listar";
    }

    // Formulario para crear rol
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        return "roles/formulario";
    }

    // Guardar nuevo rol
    @PostMapping
    public String guardar(@ModelAttribute Rol rol) {
        rolService.crearRol(rol);
        return "redirect:/roles";
    }

    // Formulario para editar rol
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Rol> rol = rolService.obtenerRol(id);
        if (rol.isPresent()) {
            model.addAttribute("rol", rol.get());
            return "roles/formulario";
        }
        return "redirect:/roles";
    }

    // Guardar cambios del rol
    @PostMapping("/editar/{id}")
    public String actualizarRol(@PathVariable Long id, @ModelAttribute Rol rol) {
        rolService.editarRol(id, rol);
        return "redirect:/roles";
    }

    // Eliminar rol
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        rolService.eliminarRol(id);
        return "redirect:/roles";
    }
}