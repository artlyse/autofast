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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.autofast.entity.Repuesto;
import pe.edu.utp.autofast.service.RepuestoService;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private RepuestoService repuestoService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("repuestos", repuestoService.findAll());
        model.addAttribute("stockBajo", repuestoService.findStockBajo());
        model.addAttribute("view", "inventario/list");
        model.addAttribute("activePage", "inventario");
        return "layout/layout";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("repuesto", new Repuesto());
        model.addAttribute("view", "inventario/form");
        model.addAttribute("activePage", "inventario");
        return "layout/layout";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("repuesto") Repuesto repuesto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inventario/form";
        }

        try {
            if (repuestoService.existsByCodigo(repuesto.getCodigo())) {
                redirectAttributes.addFlashAttribute("error", "El código ya está registrado");
                return "redirect:/inventario/nuevo";
            }
            repuestoService.save(repuesto);
            redirectAttributes.addFlashAttribute("success", "Repuesto registrado exitosamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar el repuesto: " + e.getMessage());
            return "redirect:/inventario/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        repuestoService.findById(id).ifPresentOrElse(
            repuesto -> model.addAttribute("repuesto", repuesto),
            () -> model.addAttribute("error", "Repuesto no encontrado")
        );
        model.addAttribute("view", "inventario/form");
        model.addAttribute("activePage", "inventario");
        return "layout/layout";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("repuesto") Repuesto repuesto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inventario/form";
        }

        try {
            repuesto.setId(id);
            repuestoService.update(repuesto);
            redirectAttributes.addFlashAttribute("success", "Repuesto actualizado exitosamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el repuesto: " + e.getMessage());
            return "redirect:/inventario/editar/" + id;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            repuestoService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Repuesto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el repuesto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/agregar-stock/{id}")
    public String agregarStock(@PathVariable Long id, 
                               @RequestParam Integer cantidad,
                               RedirectAttributes redirectAttributes) {
        try {
            repuestoService.agregarStock(id, cantidad);
            redirectAttributes.addFlashAttribute("success", "Stock actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el stock: " + e.getMessage());
        }
        return "redirect:/inventario";
    }
}