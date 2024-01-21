package jewellery.jewellery.Services;

import jewellery.jewellery.DTO.ParticipantsDTO;
import jewellery.jewellery.MainTelegramBot;
import jewellery.jewellery.Models.Participants;
import jewellery.jewellery.Repositories.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantsService {
    
    private final ParticipantsRepository participantsRepository;
    private final MainTelegramBot telegramBot;

    //------------------------------------CRUD-----------------------------------------//

    public String participantRegistration(ParticipantsDTO participantsDTO) {

        Participants participant = new Participants();

        participant.setFullName(participantsDTO.getFullName());
        participant.setEmail(participantsDTO.getEmail());
        participant.setPhone(participantsDTO.getPhone());
        participant.setMessage(participantsDTO.getMessage());
        participant.setCreationTime(LocalDateTime.now());

        participantsRepository.save(participant);

        SendMessage message = new SendMessage();

        message.setChatId("-1001991811481");
        message.setText("A new participant has registered:\n--------------------------------\n\nFull Name: "
                + participant.getFullName() + "\nEmail: " + participant.getEmail()
                + "\nMessage: " + participant.getMessage());

        telegramBot.sendMessage(message);

        return "You Successfully Registered!";
    }

    public String updateParticipant(Long id, ParticipantsDTO participantsDTO) {

        Participants participant = participantsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        if (participantsDTO.getEmail() != null) {
            participant.setEmail(participantsDTO.getEmail());
        }

        if (participantsDTO.getPhone() != null) {
            participant.setPhone(participantsDTO.getPhone());
        }

        if (participantsDTO.getFullName() != null) {
            participant.setFullName(participantsDTO.getFullName());
        }

        if (participantsDTO.getMessage() != null) {
            participant.setMessage(participantsDTO.getMessage());
        }

        participantsRepository.save(participant);

        return "You Successfully Updated Participant!";
    }

    public String deleteParticipant(Long id) {

        Participants participant = participantsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        participantsRepository.delete(participant);

        return "You Successfully Deleted " + participant.getFullName() + "!";
    }

    //-----------------------------------VIEWS-------------------------------------------//

    public List<ParticipantsDTO> allParticipants() {

        return participantsRepository.findAll(Sort.by("creationTime").ascending())
                .stream().map(ParticipantsDTO::new).collect(Collectors.toList());
    }

    public ParticipantsDTO singleParticipant(Long id) {

        return participantsRepository.findById(id)
                .stream().map(ParticipantsDTO::new).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));
    }
}