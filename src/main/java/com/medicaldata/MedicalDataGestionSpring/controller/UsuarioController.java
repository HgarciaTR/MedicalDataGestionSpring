/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Controlador encargado de gestionar los usuarios del sistema.
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    // Comprueba si existe una sesión de administrador.
    private boolean esAdministrador(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        return usuario != null && "ADMIN".equals(usuario.getRol());
    }

    // Lista los usuarios. Solo ADMIN puede acceder.
    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        model.addAttribute("usuarios", usuarioRepo.findAll());

        return "usuarios/index";
    }

    // Formulario interno para que ADMIN registre usuarios.
    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(HttpSession session, Model model) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        model.addAttribute("usuario", new Usuario());

        return "usuarios/nuevo";
    }

    // Registra un usuario desde el área administrativa.
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(
            @ModelAttribute Usuario usuario,
            HttpSession session) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        usuario.setRol("USUARIO");

        usuario.setFechaRegistro(
                Date.valueOf(java.time.LocalDate.now())
        );

        usuarioRepo.save(usuario);

        return "redirect:/usuarios";
    }

    // Busca un usuario para editarlo.
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        Usuario usuario = usuarioRepo.findById(id).orElse(null);

        model.addAttribute("usuario", usuario);

        return "usuarios/editar";
    }

    // Actualiza un usuario.
    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(
            @ModelAttribute Usuario usuario,
            HttpSession session) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        usuarioRepo.save(usuario);

        return "redirect:/usuarios";
    }

    // Elimina un usuario.
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(
            @PathVariable Integer id,
            HttpSession session) {

        if (!esAdministrador(session)) {
            return "redirect:/panel";
        }

        usuarioRepo.deleteById(id);

        return "redirect:/usuarios";
    }

}