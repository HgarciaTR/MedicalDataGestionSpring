package com.medicaldata.MedicalDataGestionSpring.repository;

import com.medicaldata.MedicalDataGestionSpring.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio encargado de administrar los datos de la entidad Usuario.
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Busca un usuario utilizando su correo y contraseña.
    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);

    // Busca un usuario utilizando solamente su correo.
    Optional<Usuario> findByCorreo(String correo);

}