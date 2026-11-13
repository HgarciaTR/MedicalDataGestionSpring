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
import jakarta.persistence.Transient;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Fecha y hora en que se registró el medicamento.
    @Column(name = "fechaRegistro")
    private LocalDateTime fechaRegistro;

    // Fecha y hora de la última actualización del medicamento.
    @Column(name = "fechaActualizacion")
    private LocalDateTime fechaActualizacion;

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

    // Obtiene la fecha de registro del medicamento.
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    // Asigna la fecha de registro del medicamento.
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Obtiene la fecha de la última actualización.
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    // Asigna la fecha de la última actualización.
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Obtiene el estado del medicamento según su fecha de vencimiento.
    @Transient
    public String getEstadoVencimiento() {

        // Comprueba si existe una fecha de vencimiento.
        if (fechaVencimiento == null) {
            return "Sin fecha";
        }

        // Obtiene la fecha actual y la fecha de vencimiento.
        LocalDate fechaActual = LocalDate.now();
        LocalDate vencimiento = fechaVencimiento.toLocalDate();

        // Comprueba si el medicamento se encuentra vencido.
        if (vencimiento.isBefore(fechaActual)) {
            return "Vencido";
        }

        // Comprueba si el medicamento vence en los próximos 30 días.
        if (!vencimiento.isAfter(fechaActual.plusDays(30))) {
            return "Por vencer";
        }

        // Indica que el medicamento se encuentra vigente.
        return "Vigente";
    }

}