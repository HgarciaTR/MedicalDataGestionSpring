/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador encargado de mostrar el panel principal.
@Controller
public class PanelController {

    @GetMapping("/panel")
    public String mostrarPanel(
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // Indica si el usuario actual es administrador.
        boolean administrador = "ADMIN".equals(usuario.getRol());

        model.addAttribute("administrador", administrador);

        return "panel";
    }

}