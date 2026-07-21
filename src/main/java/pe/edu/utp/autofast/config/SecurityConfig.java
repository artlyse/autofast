package pe.edu.utp.autofast.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import pe.edu.utp.autofast.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            return usuarioRepository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                    .password(u.getPassword())
                    .roles(u.getRol())
                    .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/login").permitAll()
                
                // Roles y permisos
                .requestMatchers("/clientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                .requestMatchers("/vehiculos/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                .requestMatchers("/ordenes/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "TECNICO")
                .requestMatchers("/inventario/**").hasAnyRole("ADMIN", "ALMACENERO")
                .requestMatchers("/reportes/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasRole("ADMIN")  // Solo admin gestiona usuarios
                .requestMatchers("/auditoria/**").hasRole("ADMIN") // Solo admin ve auditoría
                .requestMatchers("/").hasAnyRole("ADMIN", "RECEPCIONISTA", "TECNICO", "ALMACENERO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/acceso-denegado");
        };
    }
}