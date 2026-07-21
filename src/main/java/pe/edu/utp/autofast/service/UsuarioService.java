package pe.edu.utp.autofast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.autofast.entity.Tecnico;
import pe.edu.utp.autofast.entity.Usuario;
import pe.edu.utp.autofast.repository.TecnicoRepository;
import pe.edu.utp.autofast.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findByActivoTrueOrderByNombreAsc();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        // Encriptar contraseña si es nueva
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            if (!usuario.getPassword().startsWith("$2a$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        Usuario saved = usuarioRepository.save(usuario);
        
        // Sincronizar con tabla tecnico si el rol es TECNICO
        sincronizarTecnico(saved);
        
        return saved;
    }

    @Transactional
    public Usuario update(Usuario usuario) {
        Optional<Usuario> existing = usuarioRepository.findById(usuario.getId());
        if (existing.isPresent()) {
            Usuario existingUser = existing.get();
            
            // Mantener contraseña si no se cambia
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                if (!usuario.getPassword().startsWith("$2a$")) {
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }
            } else {
                usuario.setPassword(existingUser.getPassword());
            }
            
            // Mantener fecha de registro
            usuario.setFechaRegistro(existingUser.getFechaRegistro());
        }
        
        Usuario updated = usuarioRepository.save(usuario);
        
        // Sincronizar con tabla tecnico
        sincronizarTecnico(updated);
        
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        // Desactivar técnico asociado
        Optional<Tecnico> tecnico = tecnicoRepository.findByEmail(usuario.getEmail());
        tecnico.ifPresent(t -> {
            t.setActivo(false);
            tecnicoRepository.save(t);
        });
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public long count() {
        return usuarioRepository.count();
    }

    /**
     * Sincroniza la tabla tecnico con el usuario si tiene rol TECNICO
     */
    private void sincronizarTecnico(Usuario usuario) {
        if ("TECNICO".equals(usuario.getRol()) && usuario.getActivo()) {
            // Buscar si ya existe un técnico con este email
            Optional<Tecnico> existingTecnico = tecnicoRepository.findByEmail(usuario.getEmail());
            
            if (existingTecnico.isPresent()) {
                // Actualizar técnico existente
                Tecnico tecnico = existingTecnico.get();
                tecnico.setNombre(usuario.getNombre());
                tecnico.setApellido(usuario.getApellido());
                tecnico.setEmail(usuario.getEmail());
                tecnico.setActivo(true);
                tecnicoRepository.save(tecnico);
                System.out.println("✅ Técnico actualizado: " + usuario.getEmail());
            } else {
                // Crear nuevo técnico
                Tecnico nuevoTecnico = new Tecnico();
                nuevoTecnico.setNombre(usuario.getNombre());
                nuevoTecnico.setApellido(usuario.getApellido());
                nuevoTecnico.setEspecialidad("Mecánica General");
                nuevoTecnico.setTelefono("987654321");
                nuevoTecnico.setEmail(usuario.getEmail());
                nuevoTecnico.setActivo(true);
                tecnicoRepository.save(nuevoTecnico);
                System.out.println("✅ Nuevo técnico creado: " + usuario.getEmail());
            }
        } else {
            // Si el usuario no es TECNICO o está inactivo, desactivar su técnico
            Optional<Tecnico> tecnico = tecnicoRepository.findByEmail(usuario.getEmail());
            tecnico.ifPresent(t -> {
                t.setActivo(false);
                tecnicoRepository.save(t);
                System.out.println("⚠️ Técnico desactivado: " + usuario.getEmail());
            });
        }
    }
}
