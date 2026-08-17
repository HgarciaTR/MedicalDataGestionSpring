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

// Controlador encargado de mostrar el perfil del usuario.
@Controller
public class PerfilController {

    // Muestra la información del usuario que inició sesión.
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {

        // Obtener el usuario almacenado en la sesión.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si no existe una sesión activa, regresar al login.
        if (usuario == null) {
            return "redirect:/login";
        }

        // Enviar el usuario a la vista.
        model.addAttribute("usuario", usuario);

        // Mostrar la página de perfil.
        return "perfil";
    }

}