package com.unlar.guarderia.Controllers;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.unlar.guarderia.Entitites.PushSubscription;
import com.unlar.guarderia.Entitites.Tutor;
import com.unlar.guarderia.Repositories.PushSubscriptionRepository;
import com.unlar.guarderia.Repositories.TutorRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor
public class PushController {

    private final PushSubscriptionRepository pushRepo;
    private final TutorRepository tutorRepository;

@PostMapping("/guardar")
public ResponseEntity<String> guardar(@RequestBody Map<String, Object> body, Authentication auth) {
    String username = auth.getName();
    Tutor tutor = tutorRepository.findByUsuario_Email(username);

    if (tutor != null) {
        String endpoint = (String) body.get("endpoint");
  
        @SuppressWarnings("unchecked")
        Map<String, String> keys = (Map<String, String>) body.get("keys");
        
        PushSubscription sub = new PushSubscription();
        sub.setEndpoint(endpoint);
        sub.setP256dh(keys.get("p256dh"));
        sub.setAuth(keys.get("auth"));
        sub.setTutor(tutor);
        
        pushRepo.save(sub);
        return ResponseEntity.ok("Suscripción guardada");
    }
    return ResponseEntity.status(404).body("Tutor no encontrado");
}
}