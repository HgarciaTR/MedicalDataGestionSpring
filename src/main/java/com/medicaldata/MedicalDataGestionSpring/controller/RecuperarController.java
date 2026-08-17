package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador encargado del proceso de recuperación de contraseña.
@Controller
public class RecuperarController {

    // Permite consultar los usuarios registrados.
    @Autowired
    private UsuarioRepository usuarioRepo;

    // Muestra el formulario para solicitar recuperación.
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {

        return "recuperar";
    }

    // Verifica si el correo existe.
    @PostMapping("/recuperar/verificar")
    public String verificarCorreo(
            @RequestParam String correo,
            Model model) {

        // Buscar el usuario por correo.
        Optional<Usuario> resultado = usuarioRepo.findByCorreo(correo);

        // Comprobar si el correo existe.
        if (resultado.isPresent()) {

            // Enviar el correo encontrado a la siguiente vista.
            model.addAttribute("correo", correo);

            return "nueva-contrasena";
        }

        // Mostrar mensaje si el correo no está registrado.
        model.addAttribute(
                "error",
                "No existe un usuario registrado con ese correo."
        );

        return "recuperar";
    }

    // Cambia la contraseña del usuario.
    @PostMapping("/recuperar/cambiar")
    public String cambiarContrasena(
            @RequestParam String correo,
            @RequestParam String nuevaContrasena,
            Model model) {

        // Buscar el usuario por correo.
        Optional<Usuario> resultado = usuarioRepo.findByCorreo(correo);

        if (resultado.isPresent()) {

            // Obtener el usuario encontrado.
            Usuario usuario = resultado.get();

            // Asignar la nueva contraseña.
            usuario.setContrasena(nuevaContrasena);

            // Guardar el cambio en la base de datos.
            usuarioRepo.save(usuario);

            // Mostrar mensaje de confirmación.
            model.addAttribute(
                    "mensaje",
                    "La contraseña fue actualizada correctamente."
            );

            return "login";
        }

        // Si el usuario no existe, regresar a recuperación.
        model.addAttribute(
                "error",
                "No se pudo actualizar la contraseña."
        );

        return "recuperar";
    }

}