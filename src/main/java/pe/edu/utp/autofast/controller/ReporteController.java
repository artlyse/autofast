package pe.edu.utp.autofast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.edu.utp.autofast.service.ClienteService;
import pe.edu.utp.autofast.service.OrdenService;
import pe.edu.utp.autofast.service.RepuestoService;
import pe.edu.utp.autofast.service.VehiculoService;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private RepuestoService repuestoService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("totalClientes", clienteService.count());
        model.addAttribute("totalVehiculos", vehiculoService.count());
        model.addAttribute("totalOrdenes", ordenService.count());
        model.addAttribute("totalRepuestos", repuestoService.count());
        model.addAttribute("ordenesPendientes", ordenService.countByEstado("Pendiente"));
        model.addAttribute("ordenesDiagnostico", ordenService.countByEstado("Diagnosticado"));
        model.addAttribute("ordenesReparacion", ordenService.countByEstado("En reparación"));
        model.addAttribute("ordenesFinalizadas", ordenService.countByEstado("Finalizado"));
        model.addAttribute("stockBajo", repuestoService.findStockBajo());
        model.addAttribute("view", "reportes/index");
        model.addAttribute("activePage", "reportes");
        return "layout/layout";
    }
}