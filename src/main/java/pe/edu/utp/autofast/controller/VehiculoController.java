package pe.edu.utp.autofast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.autofast.entity.Vehiculo;
import pe.edu.utp.autofast.service.ClienteService;
import pe.edu.utp.autofast.service.VehiculoService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vehiculos", vehiculoService.findAll());
        model.addAttribute("view", "vehiculos/list");
        model.addAttribute("activePage", "vehiculos");
        return "layout/layout";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Vehiculo vehiculo = new Vehiculo();
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("view", "vehiculos/form");
        model.addAttribute("activePage", "vehiculos");
        return "layout/layout";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("view", "vehiculos/form");
            model.addAttribute("activePage", "vehiculos");
            return "layout/layout";
        }

        try {
            if (vehiculoService.existsByPlaca(vehiculo.getPlaca())) {
                redirectAttributes.addFlashAttribute("error", "La placa ya está registrada");
                return "redirect:/vehiculos/nuevo";
            }
            vehiculoService.save(vehiculo);
            redirectAttributes.addFlashAttribute("success", "Vehículo registrado exitosamente");
            return "redirect:/vehiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar el vehículo: " + e.getMessage());
            return "redirect:/vehiculos/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        vehiculoService.findById(id).ifPresentOrElse(
            vehiculo -> {
                model.addAttribute("vehiculo", vehiculo);
                model.addAttribute("clientes", clienteService.findAll());
            },
            () -> model.addAttribute("error", "Vehículo no encontrado")
        );
        model.addAttribute("view", "vehiculos/form");
        model.addAttribute("activePage", "vehiculos");
        return "layout/layout";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("view", "vehiculos/form");
            model.addAttribute("activePage", "vehiculos");
            return "layout/layout";
        }

        try {
            vehiculo.setId(id);
            vehiculoService.update(vehiculo);
            redirectAttributes.addFlashAttribute("success", "Vehículo actualizado exitosamente");
            return "redirect:/vehiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el vehículo: " + e.getMessage());
            return "redirect:/vehiculos/editar/" + id;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculoService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Vehículo eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el vehículo: " + e.getMessage());
        }
        return "redirect:/vehiculos";
    }
}