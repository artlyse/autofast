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
import pe.edu.utp.autofast.entity.Cliente;
import pe.edu.utp.autofast.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("view", "clientes/list");
        model.addAttribute("activePage", "clientes");
        return "layout/layout";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("view", "clientes/form");
        model.addAttribute("activePage", "clientes");
        return "layout/layout";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, 
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }

        try {
            if (clienteService.existsByDniRuc(cliente.getDniRuc())) {
                redirectAttributes.addFlashAttribute("error", "El DNI/RUC ya está registrado");
                return "redirect:/clientes/nuevo";
            }
            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente registrado exitosamente");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar el cliente: " + e.getMessage());
            return "redirect:/clientes/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        clienteService.findById(id).ifPresentOrElse(
            cliente -> model.addAttribute("cliente", cliente),
            () -> model.addAttribute("error", "Cliente no encontrado")
        );
        model.addAttribute("view", "clientes/form");
        model.addAttribute("activePage", "clientes");
        return "layout/layout";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, 
                             @Valid @ModelAttribute("cliente") Cliente cliente,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/form";
        }

        try {
            cliente.setId(id);
            clienteService.update(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado exitosamente");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el cliente: " + e.getMessage());
            return "redirect:/clientes/editar/" + id;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String term, Model model) {
        model.addAttribute("clientes", clienteService.search(term));
        model.addAttribute("view", "clientes/list");
        model.addAttribute("activePage", "clientes");
        return "layout/layout";
    }
}