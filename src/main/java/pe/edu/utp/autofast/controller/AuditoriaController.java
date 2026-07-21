package pe.edu.utp.autofast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.edu.utp.autofast.service.AuditoriaService;

@Controller
@RequestMapping("/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("registros", auditoriaService.findAll());
        model.addAttribute("view", "auditoria/index");
        model.addAttribute("activePage", "auditoria");
        return "layout/layout";
    }
}