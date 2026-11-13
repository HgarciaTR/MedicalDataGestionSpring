/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.repository;

import com.medicaldata.MedicalDataGestionSpring.Medicamento;
import com.medicaldata.MedicalDataGestionSpring.Usuario;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio encargado de administrar los datos de la entidad Medicamento.
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    // Comprueba si el usuario ya tiene registrado el mismo medicamento.
    boolean existsByNombreMedicamentoIgnoreCaseAndFechaVencimientoAndUsuario(
            String nombreMedicamento,
            Date fechaVencimiento,
            Usuario usuario);

    // Comprueba si existe un medicamento duplicado durante la actualización.
    boolean existsByNombreMedicamentoIgnoreCaseAndFechaVencimientoAndUsuarioAndIdMedicamentoNot(
            String nombreMedicamento,
            Date fechaVencimiento,
            Usuario usuario,
            Integer idMedicamento);

    // Busca medicamentos por nombre.
    List<Medicamento> findByNombreMedicamentoContainingIgnoreCase(
            String nombreMedicamento);

    // Obtiene los medicamentos registrados por un usuario.
    List<Medicamento> findByUsuario(Usuario usuario);

    // Busca medicamentos por nombre entre los registros de un usuario.
    List<Medicamento> findByNombreMedicamentoContainingIgnoreCaseAndUsuario(
            String nombreMedicamento,
            Usuario usuario);

}