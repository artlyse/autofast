package pe.edu.utp.autofast.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.autofast.entity.Usuario;
import pe.edu.utp.autofast.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("view", "usuarios/list");
        model.addAttribute("activePage", "usuarios");
        return "layout/layout";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("view", "usuarios/form");
        model.addAttribute("activePage", "usuarios");
        return "layout/layout";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }

        try {
            if (usuarioService.existsByUsername(usuario.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya existe");
                return "redirect:/usuarios/nuevo";
            }
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente. Se ha sincronizado con la tabla de técnicos si corresponde.");
            return "redirect:/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            return "redirect:/usuarios/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        usuarioService.findById(id).ifPresentOrElse(
            usuario -> model.addAttribute("usuario", usuario),
            () -> model.addAttribute("error", "Usuario no encontrado")
        );
        model.addAttribute("view", "usuarios/form");
        model.addAttribute("activePage", "usuarios");
        return "layout/layout";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("usuario") Usuario usuario,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }

        try {
            usuario.setId(id);
            usuarioService.update(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente. Sincronización de técnico realizada.");
            return "redirect:/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            return "redirect:/usuarios/editar/" + id;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente. Técnico desactivado si correspondía.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
