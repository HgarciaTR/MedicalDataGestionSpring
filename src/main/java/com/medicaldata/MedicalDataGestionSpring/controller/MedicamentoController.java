/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Medicamento;
import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.MedicamentoRepository;
import com.medicaldata.MedicalDataGestionSpring.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Controlador encargado de gestionar las solicitudes del módulo de medicamentos.
@Controller
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/medicamentos")
    public String listarMedicamentos(Model model) {

        model.addAttribute("medicamentos", medicamentoRepo.findAll());

        return "index";
    }

    @GetMapping("/medicamentos/nuevo")
    public String nuevoMedicamento(Model model) {

        model.addAttribute("medicamento", new Medicamento());

        return "nuevo";
    }

    @PostMapping("/medicamentos/guardar")
    public String guardarMedicamento(
            @ModelAttribute Medicamento medicamento,
            HttpSession session) {

        // Obtener el usuario que inició sesión.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Asociar automáticamente el medicamento con ese usuario.
        medicamento.setUsuario(usuario);

        // Guardar el medicamento.
        medicamentoRepo.save(medicamento);

        return "redirect:/medicamentos";
    }

    @GetMapping("/medicamentos/editar/{id}")
    public String editarMedicamento(@PathVariable Integer id, Model model) {

        Medicamento medicamento = medicamentoRepo.findById(id).orElse(null);

        List<Usuario> usuarios = usuarioRepo.findAll();

        model.addAttribute("medicamento", medicamento);
        model.addAttribute("usuarios", usuarios);

        return "editar";
    }

    @PostMapping("/medicamentos/actualizar")
    public String actualizarMedicamento(@ModelAttribute Medicamento medicamento) {

        medicamentoRepo.save(medicamento);

        return "redirect:/medicamentos";
    }

    @GetMapping("/medicamentos/eliminar/{id}")
    public String eliminarMedicamento(@PathVariable Integer id) {

        medicamentoRepo.deleteById(id);

        return "redirect:/medicamentos";
    }

}