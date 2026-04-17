package plataforma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plataforma.domain.Rol;
import plataforma.repository.RolRepository;
import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // Listar todos los roles
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    // Obtener un rol por ID
    public Optional<Rol> obtenerRol(Long id) {
        return rolRepository.findById(id);
    }

    // Crear un nuevo rol
    public Rol crearRol(Rol rol) {
        return rolRepository.save(rol);
    }

    // Editar un rol
    public Rol editarRol(Long id, Rol rolActualizado) {
        Optional<Rol> rolExistente = rolRepository.findById(id);
        if (rolExistente.isPresent()) {
            Rol rol = rolExistente.get();
            rol.setNombre(rolActualizado.getNombre());
            return rolRepository.save(rol);
        }
        return null;
    }

    // Eliminar un rol
    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }

    // Contar total de roles
    public long contarRoles() {
        return rolRepository.count();
    }
}