package pe.edu.utp.autofast.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import pe.edu.utp.autofast.entity.Auditoria;
import pe.edu.utp.autofast.repository.AuditoriaRepository;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private HttpServletRequest request;

    public List<Auditoria> findAll() {
        return auditoriaRepository.findAllByOrderByFechaDesc();
    }

    @Transactional
    public void registrar(String accion, String entidad, Long entidadId, String detalles) {
        Auditoria auditoria = new Auditoria();
        
        // Usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditoria.setUsuario(username);
        
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setDetalles(detalles);
        
        // IP del cliente
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        auditoria.setIp(ip);
        
        auditoriaRepository.save(auditoria);
    }
}