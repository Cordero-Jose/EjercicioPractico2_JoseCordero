package plataforma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import plataforma.domain.Usuario;
import plataforma.domain.Rol;
import plataforma.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Optional<Usuario> obtenerUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    // Obtener usuario por email
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Enviar correo de confirmación
        emailService.enviarCorreoConfirmacion(usuarioGuardado);
        
        return usuarioGuardado;
    }

    // Editar un usuario
    public Usuario editarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setRol(usuarioActualizado.getRol());
            usuario.setActivo(usuarioActualizado.getActivo());
            
            // Si la contraseña fue actualizada
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }
            
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    // Eliminar un usuario
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Buscar usuarios por rol
    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Buscar usuarios activos/inactivos
    public List<Usuario> buscarActivos(Boolean activo) {
        return usuarioRepository.findByActivo(activo);
    }

    // Contar total de usuarios
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    // Contar usuarios activos
    public long contarUsuariosActivos() {
        return usuarioRepository.findByActivo(true).size();
    }
}