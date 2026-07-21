package pe.edu.utp.autofast.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.utp.autofast.entity.Tecnico;
import pe.edu.utp.autofast.entity.Usuario;
import pe.edu.utp.autofast.repository.TecnicoRepository;
import pe.edu.utp.autofast.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== EJECUTANDO DATALOADER ===");

        // ========== USUARIOS ==========
        
        // Crear o actualizar admin
        Usuario admin = usuarioRepository.findByUsername("admin").orElse(new Usuario());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNombre("Administrador");
        admin.setApellido("Sistema");
        admin.setEmail("admin@autofast.com");
        admin.setRol("ADMIN");
        admin.setActivo(true);
        usuarioRepository.save(admin);
        System.out.println("✅ Usuario admin actualizado/creado.");

        // Crear o actualizar recepcionista
        Usuario recepcionista = usuarioRepository.findByUsername("recepcionista").orElse(new Usuario());
        recepcionista.setUsername("recepcionista");
        recepcionista.setPassword(passwordEncoder.encode("recepcionista123"));
        recepcionista.setNombre("María");
        recepcionista.setApellido("López");
        recepcionista.setEmail("maria@autofast.com");
        recepcionista.setRol("RECEPCIONISTA");
        recepcionista.setActivo(true);
        usuarioRepository.save(recepcionista);
        System.out.println("✅ Usuario recepcionista actualizado/creado.");

        // Crear o actualizar tecnico1
        Usuario tecnico1 = usuarioRepository.findByUsername("tecnico").orElse(new Usuario());
        tecnico1.setUsername("tecnico");
        tecnico1.setPassword(passwordEncoder.encode("tecnico123"));
        tecnico1.setNombre("Carlos");
        tecnico1.setApellido("Ramírez");
        tecnico1.setEmail("carlos@autofast.com");
        tecnico1.setRol("TECNICO");
        tecnico1.setActivo(true);
        usuarioRepository.save(tecnico1);
        System.out.println("✅ Usuario tecnico actualizado/creado.");

        // Crear o actualizar tecnico2 (técnico adicional)
        Usuario tecnico2 = usuarioRepository.findByUsername("tecnico2").orElse(new Usuario());
        tecnico2.setUsername("tecnico2");
        tecnico2.setPassword(passwordEncoder.encode("tecnico123"));
        tecnico2.setNombre("Ana");
        tecnico2.setApellido("Martínez");
        tecnico2.setEmail("ana.tecnico@autofast.com");
        tecnico2.setRol("TECNICO");
        tecnico2.setActivo(true);
        usuarioRepository.save(tecnico2);
        System.out.println("✅ Usuario tecnico2 actualizado/creado.");

        // Crear o actualizar almacenero
        Usuario almacenero = usuarioRepository.findByUsername("almacenero").orElse(new Usuario());
        almacenero.setUsername("almacenero");
        almacenero.setPassword(passwordEncoder.encode("almacenero123"));
        almacenero.setNombre("Ana");
        almacenero.setApellido("Martínez");
        almacenero.setEmail("ana@autofast.com");
        almacenero.setRol("ALMACENERO");
        almacenero.setActivo(true);
        usuarioRepository.save(almacenero);
        System.out.println("✅ Usuario almacenero actualizado/creado.");

        // ========== SINCRONIZAR TÉCNICOS ==========
        // Esto asegura que los usuarios con rol TECNICO tengan su registro en la tabla tecnico
        System.out.println("=== SINCRONIZANDO TÉCNICOS ===");
        sincronizarTecnicos();

        System.out.println("=== DATALOADER FINALIZADO ===");
    }

    /**
     * Sincroniza todos los usuarios con rol TECNICO a la tabla tecnico
     */
    private void sincronizarTecnicos() {
        // Buscar todos los usuarios activos con rol TECNICO
        List<Usuario> tecnicosUsuarios = usuarioRepository.findAll().stream()
                .filter(u -> "TECNICO".equals(u.getRol()) && u.getActivo())
                .collect(Collectors.toList());

        for (Usuario usuario : tecnicosUsuarios) {
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
        }

        // Desactivar técnicos que ya no son usuarios TECNICO
        List<Tecnico> allTecnicos = tecnicoRepository.findAll();
        for (Tecnico tecnico : allTecnicos) {
            boolean existsAsTecnico = tecnicosUsuarios.stream()
                    .anyMatch(u -> u.getEmail().equals(tecnico.getEmail()));
            if (!existsAsTecnico && tecnico.getActivo()) {
                tecnico.setActivo(false);
                tecnicoRepository.save(tecnico);
                System.out.println("⚠️ Técnico desactivado: " + tecnico.getEmail());
            }
        }
    }
}
