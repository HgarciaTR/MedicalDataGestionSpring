/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Date;

// Entidad que representa la tabla MEDICAMENTO de la base de datos.
@Entity
@Table(name = "MEDICAMENTO")
public class Medicamento {

    // Identificador principal del medicamento.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMEDICAMENTO")
    private Integer idMedicamento;

    // Nombre del medicamento registrado.
    @Column(name = "nombreMedicamento")
    private String nombreMedicamento;

    // Fecha de vencimiento del medicamento.
    @Column(name = "fechaVencimiento")
    private Date fechaVencimiento;

    // Descripción del medicamento.
    @Column(name = "descripcion")
    private String descripcion;

    // Usuario que registró el medicamento.
    @ManyToOne
    @JoinColumn(name = "USUARIO_idUSUARIO")
    private Usuario usuario;

    // Obtiene el identificador del medicamento.
    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    // Asigna el identificador del medicamento.
    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    // Obtiene el nombre del medicamento.
    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    // Asigna el nombre del medicamento.
    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    // Obtiene la fecha de vencimiento.
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    // Asigna la fecha de vencimiento.
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    // Obtiene la descripción del medicamento.
    public String getDescripcion() {
        return descripcion;
    }

    // Asigna la descripción del medicamento.
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Obtiene el usuario que registró el medicamento.
    public Usuario getUsuario() {
        return usuario;
    }

    // Asigna el usuario que registró el medicamento.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}