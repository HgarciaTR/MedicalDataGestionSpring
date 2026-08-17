/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.repository;

import com.medicaldata.MedicalDataGestionSpring.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio encargado de administrar los datos de la entidad Medicamento.
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

}
