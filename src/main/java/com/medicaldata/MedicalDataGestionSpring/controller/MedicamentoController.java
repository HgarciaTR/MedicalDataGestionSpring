/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.medicaldata.MedicalDataGestionSpring.controller;

import com.medicaldata.MedicalDataGestionSpring.Medicamento;
import com.medicaldata.MedicalDataGestionSpring.Usuario;
import com.medicaldata.MedicalDataGestionSpring.repository.MedicamentoRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador encargado de gestionar las solicitudes del módulo de medicamentos.
@Controller
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepo;

    // Comprueba si el usuario tiene el rol de administrador.
    private boolean esAdministrador(Usuario usuario) {

        return usuario != null && "ADMIN".equals(usuario.getRol());
    }

    // Comprueba si el usuario tiene permiso para gestionar el medicamento.
    private boolean tienePermiso(Usuario usuario, Medicamento medicamento) {

        if (usuario == null || medicamento == null) {
            return false;
        }

        if (esAdministrador(usuario)) {
            return true;
        }

        return medicamento.getUsuario() != null
                && Objects.equals(
                        usuario.getIdUsuario(),
                        medicamento.getUsuario().getIdUsuario());
    }

    // Lista los medicamentos y permite realizar búsquedas por nombre.
    @GetMapping("/medicamentos")
    public String listarMedicamentos(
            @RequestParam(name = "buscar", defaultValue = "") String buscar,
            HttpSession session,
            Model model) {

        // Obtiene el usuario que inició sesión.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Medicamento> todos;
        List<Medicamento> medicamentos;

        buscar = buscar.trim();

        // El administrador puede consultar todos los medicamentos.
        if (esAdministrador(usuario)) {

            todos = medicamentoRepo.findAll();

            if (buscar.isEmpty()) {
                medicamentos = todos;
            } else {
                medicamentos = medicamentoRepo
                        .findByNombreMedicamentoContainingIgnoreCase(buscar);
            }

        } else {

            // El usuario normal solamente consulta sus medicamentos.
            todos = medicamentoRepo.findByUsuario(usuario);

            if (buscar.isEmpty()) {
                medicamentos = todos;
            } else {
                medicamentos = medicamentoRepo
                        .findByNombreMedicamentoContainingIgnoreCaseAndUsuario(
                                buscar, usuario);
            }
        }

        // Contadores de medicamentos según su estado.
        int vigentes = 0;
        int porVencer = 0;
        int vencidos = 0;

        // Recorre los medicamentos para calcular los indicadores.
        for (Medicamento medicamento : todos) {

            String estado = medicamento.getEstadoVencimiento();

            if ("Vigente".equals(estado)) {
                vigentes++;
            } else if ("Por vencer".equals(estado)) {
                porVencer++;
            } else if ("Vencido".equals(estado)) {
                vencidos++;
            }
        }

        // Envía la información a la vista de medicamentos.
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("buscar", buscar);
        model.addAttribute("total", todos.size());
        model.addAttribute("vigentes", vigentes);
        model.addAttribute("porVencer", porVencer);
        model.addAttribute("vencidos", vencidos);
        model.addAttribute("administrador", esAdministrador(usuario));

        return "index";
    }

    // Abre el formulario para registrar un medicamento.
    @GetMapping("/medicamentos/nuevo")
    public String nuevoMedicamento(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("medicamento", new Medicamento());

        return "nuevo";
    }

    // Registra un medicamento y comprueba que no esté duplicado.
    @PostMapping("/medicamentos/guardar")
    public String guardarMedicamento(
            @ModelAttribute Medicamento medicamento,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Obtiene el usuario que inició sesión.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Comprueba que los campos obligatorios estén completos.
        if (medicamento.getNombreMedicamento() == null
                || medicamento.getNombreMedicamento().trim().isEmpty()
                || medicamento.getFechaVencimiento() == null) {

            model.addAttribute("error",
                    "Debe ingresar el nombre y la fecha de vencimiento.");

            return "nuevo";
        }

        // Elimina espacios innecesarios del nombre.
        medicamento.setNombreMedicamento(
                medicamento.getNombreMedicamento().trim());

        // Comprueba si el medicamento ya se encuentra registrado.
        boolean duplicado = medicamentoRepo
                .existsByNombreMedicamentoIgnoreCaseAndFechaVencimientoAndUsuario(
                        medicamento.getNombreMedicamento(),
                        medicamento.getFechaVencimiento(),
                        usuario);

        if (duplicado) {

            model.addAttribute("error",
                    "Ya existe un medicamento con el mismo nombre y fecha de vencimiento.");

            return "nuevo";
        }

        // Prepara un nuevo registro con los datos del formulario.
        Medicamento nuevo = new Medicamento();

        nuevo.setNombreMedicamento(medicamento.getNombreMedicamento());
        nuevo.setFechaVencimiento(medicamento.getFechaVencimiento());
        nuevo.setDescripcion(medicamento.getDescripcion());

        // Asocia el medicamento con el usuario que inició sesión.
        nuevo.setUsuario(usuario);

        // Registra automáticamente la fecha y hora de creación.
        nuevo.setFechaRegistro(LocalDateTime.now());

        // Guarda el medicamento en la base de datos.
        medicamentoRepo.save(nuevo);

        redirectAttributes.addFlashAttribute("mensaje",
                "Medicamento registrado correctamente.");

        return "redirect:/medicamentos";
    }

    // Busca un medicamento para editarlo.
    @GetMapping("/medicamentos/editar/{id}")
    public String editarMedicamento(
            @PathVariable Integer id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        Medicamento medicamento = medicamentoRepo.findById(id).orElse(null);

        // Comprueba si el usuario tiene permiso para editar el registro.
        if (!tienePermiso(usuario, medicamento)) {

            redirectAttributes.addFlashAttribute("error",
                    "No tiene permiso para editar este medicamento o el registro no existe.");

            return "redirect:/medicamentos";
        }

        model.addAttribute("medicamento", medicamento);

        return "editar";
    }

    // Actualiza un medicamento registrado.
    @PostMapping("/medicamentos/actualizar")
    public String actualizarMedicamento(
            @ModelAttribute Medicamento medicamento,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Busca el medicamento original en la base de datos.
        Medicamento original = medicamento.getIdMedicamento() == null
                ? null
                : medicamentoRepo.findById(
                        medicamento.getIdMedicamento()).orElse(null);

        // Comprueba si el usuario tiene permiso para actualizar el registro.
        if (!tienePermiso(usuario, original)) {

            redirectAttributes.addFlashAttribute("error",
                    "No tiene permiso para actualizar este medicamento o el registro no existe.");

            return "redirect:/medicamentos";
        }

        // Comprueba que los campos obligatorios estén completos.
        if (medicamento.getNombreMedicamento() == null
                || medicamento.getNombreMedicamento().trim().isEmpty()
                || medicamento.getFechaVencimiento() == null) {

            model.addAttribute("medicamento", original);
            model.addAttribute("error",
                    "Debe ingresar el nombre y la fecha de vencimiento.");

            return "editar";
        }

        medicamento.setNombreMedicamento(
                medicamento.getNombreMedicamento().trim());

        // Comprueba duplicados sin incluir el medicamento que se está editando.
        boolean duplicado = medicamentoRepo
                .existsByNombreMedicamentoIgnoreCaseAndFechaVencimientoAndUsuarioAndIdMedicamentoNot(
                        medicamento.getNombreMedicamento(),
                        medicamento.getFechaVencimiento(),
                        original.getUsuario(),
                        original.getIdMedicamento());

        if (duplicado) {

            model.addAttribute("medicamento", original);
            model.addAttribute("error",
                    "Ya existe otro medicamento con el mismo nombre y fecha de vencimiento.");

            return "editar";
        }

        // Actualiza únicamente los datos permitidos del medicamento.
        original.setNombreMedicamento(medicamento.getNombreMedicamento());
        original.setFechaVencimiento(medicamento.getFechaVencimiento());
        original.setDescripcion(medicamento.getDescripcion());

        // Registra la fecha y hora de la última modificación.
        original.setFechaActualizacion(LocalDateTime.now());

        // Guarda los cambios sin modificar el usuario original.
        medicamentoRepo.save(original);

        redirectAttributes.addFlashAttribute("mensaje",
                "Medicamento actualizado correctamente.");

        return "redirect:/medicamentos";
    }

    // Elimina un medicamento mediante una solicitud POST.
    @PostMapping("/medicamentos/eliminar/{id}")
    public String eliminarMedicamento(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        Medicamento medicamento = medicamentoRepo.findById(id).orElse(null);

        // Comprueba si el usuario tiene permiso para eliminar el registro.
        if (!tienePermiso(usuario, medicamento)) {

            redirectAttributes.addFlashAttribute("error",
                    "No tiene permiso para eliminar este medicamento o el registro no existe.");

            return "redirect:/medicamentos";
        }

        // Elimina el medicamento de la base de datos.
        medicamentoRepo.deleteById(id);

        redirectAttributes.addFlashAttribute("mensaje",
                "Medicamento eliminado correctamente.");

        return "redirect:/medicamentos";
    }

}