package pe.edu.utp.autofast.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pe.edu.utp.autofast.entity.Usuario;
import pe.edu.utp.autofast.repository.UsuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== EJECUTANDO DATALOADER ===");

        // Actualizar o crear admin
        Usuario admin = usuarioRepository.findByUsername("admin").orElse(new Usuario());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));  // Hash correcto para admin123
        admin.setNombre("Administrador");
        admin.setApellido("Sistema");
        admin.setEmail("admin@autofast.com");
        admin.setRol("ADMIN");
        admin.setActivo(true);
        usuarioRepository.save(admin);
        System.out.println("✅ Usuario admin actualizado/creado.");

        // Actualizar o crear recepcionista
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

        // Actualizar o crear tecnico
        Usuario tecnico = usuarioRepository.findByUsername("tecnico").orElse(new Usuario());
        tecnico.setUsername("tecnico");
        tecnico.setPassword(passwordEncoder.encode("tecnico123"));
        tecnico.setNombre("Carlos");
        tecnico.setApellido("Ramírez");
        tecnico.setEmail("carlos@autofast.com");
        tecnico.setRol("TECNICO");
        tecnico.setActivo(true);
        usuarioRepository.save(tecnico);
        System.out.println("✅ Usuario tecnico actualizado/creado.");

        // Actualizar o crear almacenero
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

        System.out.println("=== DATALOADER FINALIZADO ===");
    }
}