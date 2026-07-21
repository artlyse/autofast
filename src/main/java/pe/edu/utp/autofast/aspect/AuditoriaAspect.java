package pe.edu.utp.autofast.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.edu.utp.autofast.entity.Cliente;
import pe.edu.utp.autofast.entity.OrdenServicio;
import pe.edu.utp.autofast.entity.Repuesto;
import pe.edu.utp.autofast.entity.Usuario;
import pe.edu.utp.autofast.entity.Vehiculo;
import pe.edu.utp.autofast.service.AuditoriaService;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    // ===== CLIENTES =====
    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.ClienteService.save(..))", returning = "result")
    public void logClienteSave(JoinPoint jp, Object result) {
        Cliente cliente = (Cliente) result;
        String accion = cliente.getId() != null ? "CREAR" : "ACTUALIZAR";
        auditoriaService.registrar(accion, "CLIENTE", cliente.getId(), 
            "Cliente: " + cliente.getNombreCompleto() + " - " + cliente.getDniRuc());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.ClienteService.update(..))", returning = "result")
    public void logClienteUpdate(JoinPoint jp, Object result) {
        Cliente cliente = (Cliente) result;
        auditoriaService.registrar("ACTUALIZAR", "CLIENTE", cliente.getId(), 
            "Cliente: " + cliente.getNombreCompleto() + " - " + cliente.getDniRuc());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.ClienteService.delete(..))", returning = "result")
    public void logClienteDelete(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Long id = (Long) args[0];
        auditoriaService.registrar("ELIMINAR", "CLIENTE", id, "Cliente ID: " + id);
    }

    // ===== VEHÍCULOS =====
    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.VehiculoService.save(..))", returning = "result")
    public void logVehiculoSave(JoinPoint jp, Object result) {
        Vehiculo vehiculo = (Vehiculo) result;
        String accion = vehiculo.getId() != null ? "CREAR" : "ACTUALIZAR";
        auditoriaService.registrar(accion, "VEHICULO", vehiculo.getId(), 
            "Vehículo: " + vehiculo.getPlaca() + " - " + vehiculo.getMarca() + " " + vehiculo.getModelo());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.VehiculoService.delete(..))", returning = "result")
    public void logVehiculoDelete(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Long id = (Long) args[0];
        auditoriaService.registrar("ELIMINAR", "VEHICULO", id, "Vehículo ID: " + id);
    }

    // ===== ÓRDENES =====
    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.OrdenService.save(..))", returning = "result")
    public void logOrdenSave(JoinPoint jp, Object result) {
        OrdenServicio orden = (OrdenServicio) result;
        String accion = orden.getId() != null ? "CREAR" : "ACTUALIZAR";
        auditoriaService.registrar(accion, "ORDEN", orden.getId(), 
            "Orden: " + orden.getNumeroOrden() + " - Estado: " + orden.getEstado());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.OrdenService.actualizarEstado(..))", returning = "result")
    public void logOrdenEstado(JoinPoint jp, Object result) {
        OrdenServicio orden = (OrdenServicio) result;
        auditoriaService.registrar("CAMBIAR_ESTADO", "ORDEN", orden.getId(), 
            "Orden: " + orden.getNumeroOrden() + " - Nuevo estado: " + orden.getEstado());
    }

    // ===== REPUESTOS =====
    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.RepuestoService.save(..))", returning = "result")
    public void logRepuestoSave(JoinPoint jp, Object result) {
        Repuesto repuesto = (Repuesto) result;
        String accion = repuesto.getId() != null ? "CREAR" : "ACTUALIZAR";
        auditoriaService.registrar(accion, "REPUESTO", repuesto.getId(), 
            "Repuesto: " + repuesto.getCodigo() + " - " + repuesto.getNombre());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.RepuestoService.delete(..))", returning = "result")
    public void logRepuestoDelete(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Long id = (Long) args[0];
        auditoriaService.registrar("ELIMINAR", "REPUESTO", id, "Repuesto ID: " + id);
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.RepuestoService.agregarStock(..))", returning = "result")
    public void logRepuestoStock(JoinPoint jp, Object result) {
        Object[] args = jp.getArgs();
        Long id = (Long) args[0];
        Integer cantidad = (Integer) args[1];
        Repuesto repuesto = (Repuesto) result;
        auditoriaService.registrar("ACTUALIZAR_STOCK", "REPUESTO", id, 
            "Repuesto: " + repuesto.getCodigo() + " - Nuevo stock: " + repuesto.getStockActual());
    }

    // ===== USUARIOS =====
    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.UsuarioService.save(..))", returning = "result")
    public void logUsuarioSave(JoinPoint jp, Object result) {
        Usuario usuario = (Usuario) result;
        String accion = usuario.getId() != null ? "CREAR" : "ACTUALIZAR";
        auditoriaService.registrar(accion, "USUARIO", usuario.getId(), 
            "Usuario: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
    }

    @AfterReturning(pointcut = "execution(* pe.edu.utp.autofast.service.UsuarioService.delete(..))", returning = "result")
    public void logUsuarioDelete(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Long id = (Long) args[0];
        auditoriaService.registrar("ELIMINAR", "USUARIO", id, "Usuario ID: " + id);
    }
}