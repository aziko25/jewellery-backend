package jewellery.jewellery.Controllers;

import jewellery.jewellery.Configurations.JWTAuthorization.Authorization;
import jewellery.jewellery.DTO.ParticipantsDTO;
import jewellery.jewellery.Services.ParticipantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class ParticipantsController {

    private final ParticipantsService service;

    //------------------------------------CRUD-------------------------------------------//

    @PostMapping("/register")
    public ResponseEntity<?> createParticipant(@RequestBody ParticipantsDTO participantsDTO) {

        return ResponseEntity.ok(service.participantRegistration(participantsDTO));
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateParticipant(@PathVariable Long id, @RequestBody ParticipantsDTO participantsDTO) {

        return ResponseEntity.ok(service.updateParticipant(id, participantsDTO));
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteParticipant(id));
    }

    //--------------------------------------VIEWS--------------------------------------------//

    @Authorization(requiredRoles = {"ADMIN", "USER"})
    @GetMapping("/all")
    public ResponseEntity<?> allParticipants() {

        return ResponseEntity.ok(service.allParticipants());
    }

    @Authorization(requiredRoles = {"ADMIN", "USER"})
    @GetMapping("/{id}")
    public ResponseEntity<?> allParticipants(@PathVariable Long id) {

        return ResponseEntity.ok(service.singleParticipant(id));
    }
}