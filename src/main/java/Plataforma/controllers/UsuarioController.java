package plataforma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import plataforma.domain.Usuario;
import plataforma.domain.Rol;
import plataforma.service.UsuarioService;
import plataforma.service.RolService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // Listar todos los usuarios
    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar";
    }

    // Ver detalle de usuario
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/detalle";
        }
        return "redirect:/usuarios";
    }

    // Formulario para crear usuario
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarRoles());
        return "usuarios/formulario";
    }

    // Guardar nuevo usuario
    @PostMapping
    public String guardar(@ModelAttribute Usuario usuario, @RequestParam Long rolId) {
        Optional<Rol> rol = rolService.obtenerRol(rolId);
        if (rol.isPresent()) {
            usuario.setRol(rol.get());
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());
            usuarioService.crearUsuario(usuario);
            return "redirect:/usuarios";
        }
        return "redirect:/usuarios";
    }

    // Formulario para editar usuario
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/formulario";
        }
        return "redirect:/usuarios";
    }

    // Guardar cambios del usuario
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Usuario usuario, @RequestParam Long rolId) {
        Optional<Rol> rol = rolService.obtenerRol(rolId);
        if (rol.isPresent()) {
            usuario.setRol(rol.get());
            usuarioService.editarUsuario(id, usuario);
            return "redirect:/usuarios";
        }
        return "redirect:/usuarios";
    }

    // Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}