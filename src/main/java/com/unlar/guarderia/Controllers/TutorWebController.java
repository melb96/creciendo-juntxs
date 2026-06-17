package com.unlar.guarderia.Controllers;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutores") // URL base para este módulo web
@RequiredArgsConstructor
public class TutorWebController {

    private final TutorService tutorService;

    // 1. Pantalla que lista todos los tutores: http://localhost:8080/tutores
    @GetMapping
    public String listarTutores(Model model) {
        // Buscamos la lista en MySQL y se la inyectamos al HTML con el nombre "lista"
        model.addAttribute("lista", tutorService.obtenerTodos());
        return "tutores-lista"; // Busca templates/tutores-lista.html
    }

    // 2. Pantalla que muestra el formulario de registro: http://localhost:8080/tutores/nuevo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // Mandamos un objeto Tutor vacío para que el formulario de Thymeleaf lo "llene"
        model.addAttribute("tutor", new Tutor());
        return "tutores-formulario"; // Busca templates/tutores-formulario.html
    }

    // 3. Procesa el envío del formulario
    @PostMapping("/guardar")
    public String guardarTutor(@ModelAttribute("tutor") Tutor tutor, Model model) {
        String resultado = tutorService.registrarTutor(tutor);
        
        if (resultado.startsWith("Error")) {
            // Si el DNI ya existe, volvemos a mostrar el formulario con el mensaje de alerta
            model.addAttribute("error", resultado);
            return "tutores-formulario";
        }
        
        // Si se guardó bien, redirigimos a la tabla para ver el nuevo registro
        return "redirect:/tutores";
    }

    // 1. Muestra el formulario con los datos cargados del tutor
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
    Tutor tutor = tutorService.obtenerPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado: " + id));
    model.addAttribute("tutor", tutor);
    return "tutores-formulario"; // Reutilizamos el mismo HTML de carga
}

    // 2. Procesa la actualización
    @PostMapping("/actualizar")
    public String actualizarTutor(@ModelAttribute("tutor") Tutor tutor) {
    tutorService.actualizarTutor(tutor);
    return "redirect:/tutores";
}
// Endpoint para eliminar tutor e infantes: http://localhost:8080/tutores/eliminar/1
@GetMapping("/eliminar/{id}")
public String eliminarTutor(@PathVariable("id") Long id) {
    tutorService.eliminarTutor(id); // Recordá implementar este método común en tu TutorService (ej: tutorRepository.deleteById(id))
    return "redirect:/tutores"; // Vuelve a la tabla actualizada
}
}
