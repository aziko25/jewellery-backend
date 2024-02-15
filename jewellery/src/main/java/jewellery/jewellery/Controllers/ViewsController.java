package jewellery.jewellery.Controllers;

import jewellery.jewellery.Repositories.ParticipantsMessagesRepository;
import jewellery.jewellery.Repositories.ParticipantsRepository;
import jewellery.jewellery.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class ViewsController {

    private final ParticipantsRepository participantsRepository;
    private final ParticipantsMessagesRepository participantsMessagesRepository;

    @GetMapping("/allParticipants")
    public ResponseEntity<?> allParticipants() {

        return ResponseEntity.ok(participantsRepository.findAll(Sort.by("id")));
    }

    @GetMapping("/allParticipantsMessages")
    public ResponseEntity<?> allParticipantsMessages() {

        return ResponseEntity.ok(participantsMessagesRepository.findAll(Sort.by("id")));
    }
}