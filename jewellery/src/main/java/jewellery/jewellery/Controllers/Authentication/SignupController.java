package jewellery.jewellery.Controllers.Authentication;

import jewellery.jewellery.MainTelegramBot;
import jewellery.jewellery.Models.Participants;
import jewellery.jewellery.Repositories.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;

import static jewellery.jewellery.Utils.StaticVariables.TG_GROUP_ID;

@RestController
@RequestMapping("/api")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class SignupController {

    private final ParticipantsRepository participantsRepository;
    private final MainTelegramBot telegramBot;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Participants participant) {

        if (participant.getUserType() == null || participant.getUserType().isEmpty()) {

            throw new IllegalArgumentException("User Type Is Empty!");
        }

        String userType = participant.getUserType();

        if (userType.isEmpty() || (!userType.equalsIgnoreCase("ATTENDANT") && !userType.equalsIgnoreCase("PARTICIPANT"))) {

            throw new IllegalArgumentException("User Type Must Be Attendant Or Participant!");
        }

        Participants participants = new Participants();

        participants.setEmail(participant.getEmail());
        participants.setFullName(participant.getFullName());
        participants.setCountry(participant.getCountry());
        participants.setOrganization(participant.getOrganization());
        participants.setUserType(userType);
        participants.setRegistrationTime(LocalDateTime.now());

        String extension = null;

        if (userType.equalsIgnoreCase("PARTICIPANT")) {

            participants.setSpace(participant.getSpace());
            participants.setSpaceSubtitle(participant.getSpaceSubtitle());

            extension = "\nSpace: " + participant.getSpace() + "\nSpace Subtitle: " + participant.getSpaceSubtitle();
        }

        participantsRepository.save(participants);

        SendMessage message = new SendMessage();

        message.setChatId(TG_GROUP_ID);
        message.setText("New Participant Registered:\n--------------------------------\n\n" +
                "Type: " + participant.getUserType() +
                "\nEmail: " + participant.getEmail() +
                "\nCountry: " + participant.getCountry() +
                "\nOrganization: " + participant.getOrganization() +
                "\nFull Name: " + participant.getFullName() +
                extension);

        telegramBot.sendMessage(message);

        return ResponseEntity.ok("You Successfully Signed Up!");
    }
}