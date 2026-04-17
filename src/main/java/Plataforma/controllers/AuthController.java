package plataforma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import plataforma.domain.Usuario;
import plataforma.domain.Rol;
import plataforma.service.UsuarioService;
import plataforma.service.RolService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Página de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Página de registro
    @GetMapping("/registro")
    public String registro(Model model) {
        List<Rol> roles = rolService.listarRoles();
        model.addAttribute("roles", roles);
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Long rolId,
            Model model) {

        try {
            // Verificar si el usuario ya existe
            Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorEmail(email);
            if (usuarioExistente.isPresent()) {
                model.addAttribute("error", "El email ya está registrado");
                model.addAttribute("roles", rolService.listarRoles());
                return "registro";
            }

            // Obtener el rol
            Optional<Rol> rol = rolService.obtenerRol(rolId);
            if (rol.isEmpty()) {
                model.addAttribute("error", "Rol no válido");
                model.addAttribute("roles", rolService.listarRoles());
                return "registro";
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol(rol.get());
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());

            usuarioService.crearUsuario(nuevoUsuario);

            model.addAttribute("exito", "¡Registro exitoso! Por favor, inicia sesión.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            model.addAttribute("roles", rolService.listarRoles());
            return "registro";
        }
    }

    // Página de acceso denegado
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}