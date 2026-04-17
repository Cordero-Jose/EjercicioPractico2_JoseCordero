package plataforma.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plataforma.domain.Usuario;
import plataforma.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("🔍 Buscando usuario con email: {}", email);

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            logger.error("❌ Usuario NO encontrado: {}", email);
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        Usuario u = usuario.get();
        logger.debug("✅ Usuario encontrado: {}, Activo: {}, Rol: {}", u.getEmail(), u.isActivo(), u.getRol());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (u.getRol() != null) {
            String roleName = "ROLE_" + u.getRol().getNombre();
            logger.debug("🔐 Asignando rol: {}", roleName);
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        UserDetails userDetails = User.builder()
                .username(u.getEmail())
                .password(u.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!u.isActivo())
                .build();

        logger.debug("✅ UserDetails creado: {}", userDetails.getUsername());
        return userDetails;
    }
}
