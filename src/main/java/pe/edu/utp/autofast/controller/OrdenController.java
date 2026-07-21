package pe.edu.utp.autofast.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.autofast.entity.OrdenServicio;
import pe.edu.utp.autofast.service.ClienteService;
import pe.edu.utp.autofast.service.OrdenService;
import pe.edu.utp.autofast.service.TecnicoService;
import pe.edu.utp.autofast.service.VehiculoService;

import java.util.ArrayList;

@Controller
@RequestMapping("/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private TecnicoService tecnicoService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("ordenes", ordenService.findAll());
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar órdenes: " + e.getMessage());
            model.addAttribute("ordenes", new ArrayList<>());
        }
        model.addAttribute("view", "ordenes/list");
        model.addAttribute("activePage", "ordenes");
        return "layout/layout";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        try {
            OrdenServicio orden = new OrdenServicio();
            model.addAttribute("orden", orden);
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            model.addAttribute("tecnicos", tecnicoService.findAll());
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }
        model.addAttribute("view", "ordenes/form");
        model.addAttribute("activePage", "ordenes");
        return "layout/layout";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("orden") OrdenServicio orden,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            model.addAttribute("tecnicos", tecnicoService.findAll());
            model.addAttribute("view", "ordenes/form");
            model.addAttribute("activePage", "ordenes");
            return "layout/layout";
        }

        try {
            ordenService.save(orden);
            redirectAttributes.addFlashAttribute("success", "Orden generada exitosamente. N°: " + orden.getNumeroOrden());
            return "redirect:/ordenes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al generar la orden: " + e.getMessage());
            return "redirect:/ordenes/nueva";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        try {
            ordenService.findById(id).ifPresentOrElse(
                orden -> {
                    model.addAttribute("orden", orden);
                    model.addAttribute("clientes", clienteService.findAll());
                    model.addAttribute("vehiculos", vehiculoService.findAll());
                    model.addAttribute("tecnicos", tecnicoService.findAll());
                    
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String rol = auth.getAuthorities().iterator().next().getAuthority();
                    if ("ROLE_TECNICO".equals(rol)) {
                        model.addAttribute("tecnicoDeshabilitado", true);
                    }
                },
                () -> model.addAttribute("error", "Orden no encontrada")
            );
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la orden: " + e.getMessage());
        }
        model.addAttribute("view", "ordenes/form");
        model.addAttribute("activePage", "ordenes");
        return "layout/layout";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("orden") OrdenServicio orden,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            model.addAttribute("tecnicos", tecnicoService.findAll());
            model.addAttribute("view", "ordenes/form");
            model.addAttribute("activePage", "ordenes");
            return "layout/layout";
        }

        try {
            // Obtener la orden existente para preservar campos no editables
            OrdenServicio ordenExistente = ordenService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

            // Preservar campos que no se modifican en el formulario
            orden.setEstado(ordenExistente.getEstado());
            orden.setNumeroOrden(ordenExistente.getNumeroOrden());
            orden.setFechaApertura(ordenExistente.getFechaApertura());
            
            // Preservar técnico si no viene en el formulario
            if (orden.getTecnico() == null || orden.getTecnico().getId() == null) {
                orden.setTecnico(ordenExistente.getTecnico());
            }
            // Preservar cliente y vehículo si no vienen
            if (orden.getCliente() == null || orden.getCliente().getId() == null) {
                orden.setCliente(ordenExistente.getCliente());
            }
            if (orden.getVehiculo() == null || orden.getVehiculo().getId() == null) {
                orden.setVehiculo(ordenExistente.getVehiculo());
            }

            // Recalcular totales
            orden.setManoObra(ordenExistente.getManoObra());
            orden.setTotalRepuestos(ordenExistente.getTotalRepuestos());
            orden.calcularTotal();

            // Asignar el ID y actualizar
            orden.setId(id);
            ordenService.update(orden);
            redirectAttributes.addFlashAttribute("success", "Orden actualizada exitosamente");
            return "redirect:/ordenes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la orden: " + e.getMessage());
            return "redirect:/ordenes/editar/" + id;
        }
    }

    @GetMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id, 
                                @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        try {
            ordenService.actualizarEstado(id, estado);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado a: " + estado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado: " + e.getMessage());
        }
        return "redirect:/ordenes";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        try {
            ordenService.findById(id).ifPresentOrElse(
                orden -> {
                    model.addAttribute("orden", orden);
                    model.addAttribute("tecnicos", tecnicoService.findAll());
                },
                () -> model.addAttribute("error", "Orden no encontrada")
            );
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la orden: " + e.getMessage());
        }
        model.addAttribute("view", "ordenes/detalle");
        model.addAttribute("activePage", "ordenes");
        return "layout/layout";
    }
}
