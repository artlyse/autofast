package pe.edu.utp.autofast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pe.edu.utp.autofast.service.ClienteService;
import pe.edu.utp.autofast.service.OrdenService;
import pe.edu.utp.autofast.service.RepuestoService;
import pe.edu.utp.autofast.service.VehiculoService;

@Controller
public class DashboardController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private RepuestoService repuestoService;

    @GetMapping("/")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().iterator().next().getAuthority();

        // Estadísticas generales
        model.addAttribute("totalClientes", clienteService.count());
        model.addAttribute("totalVehiculos", vehiculoService.count());
        model.addAttribute("totalOrdenes", ordenService.count());
        model.addAttribute("totalRepuestos", repuestoService.count());
        model.addAttribute("ordenesAbiertas", ordenService.findOrdenesAbiertas());
        model.addAttribute("ordenesPendientes", ordenService.countByEstado("Pendiente"));
        model.addAttribute("ordenesDiagnostico", ordenService.countByEstado("Diagnosticado"));
        model.addAttribute("ordenesReparacion", ordenService.countByEstado("En reparación"));
        model.addAttribute("ordenesFinalizadas", ordenService.countByEstado("Finalizado"));
        model.addAttribute("stockBajo", repuestoService.findStockBajo());
        
        // Rol para personalizar mensajes o acciones
        model.addAttribute("rol", rol);
        model.addAttribute("view", "dashboard/index");
        model.addAttribute("activePage", "dashboard");
        return "layout/layout";
    }
}