/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador encargado del inicio y cierre de sesión.
@Controller
public class LoginController {

    // Permite consultar los usuarios registrados.
    @Autowired
    private UsuarioRepository usuarioRepo;

    // Muestra la pantalla de inicio de sesión.
    @GetMapping("/login")
    public String mostrarLogin(Model model) {

        return "login";
    }

    // Valida las credenciales del usuario.
    @PostMapping("/login")
    public String iniciarSesion(
            @RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session,
            Model model) {

        // Buscar al usuario por correo y contraseña.
        Optional<Usuario> resultado =
                usuarioRepo.findByCorreoAndContrasena(correo, contrasena);

        // Comprobar si existe un usuario con esos datos.
        if (resultado.isPresent()) {

            // Obtener el usuario encontrado.
            Usuario usuario = resultado.get();

            // Guardar el usuario en la sesión.
            session.setAttribute("usuario", usuario);

            // Ir al panel principal.
            return "redirect:/panel";

        }

        // Mostrar mensaje cuando las credenciales no son correctas.
        model.addAttribute(
                "error",
                "El correo o la contraseña no son correctos."
        );

        return "login";
    }

    // Cierra la sesión actual.
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {

        // Eliminar la información almacenada en la sesión.
        session.invalidate();

        // Regresar al inicio de sesión.
        return "redirect:/login";
    }

}