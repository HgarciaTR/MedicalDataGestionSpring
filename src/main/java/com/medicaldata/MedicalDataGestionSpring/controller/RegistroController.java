/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.UsuarioRepository;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// Controlador del registro público de usuarios.
@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    // Muestra el formulario público de registro.
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

    // Registra una cuenta nueva.
    @PostMapping("/registro/guardar")
    public String guardarRegistro(Usuario usuario, Model model) {

        // Los usuarios que se registran públicamente siempre serán USUARIO.
        usuario.setRol("USUARIO");

        // Registrar automáticamente la fecha.
        usuario.setFechaRegistro(
                Date.valueOf(java.time.LocalDate.now())
        );

        usuarioRepo.save(usuario);

        // Mensaje para el login.
        model.addAttribute(
                "mensaje",
                "Usuario registrado correctamente. Ahora puede iniciar sesión."
        );

        return "login";
    }

}