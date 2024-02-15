package jewellery.jewellery.Services;

import jewellery.jewellery.DTO.ParticipantsDTO;
import jewellery.jewellery.MainTelegramBot;
import jewellery.jewellery.Models.ParticipantsMessages;
import jewellery.jewellery.Repositories.ParticipantsMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static jewellery.jewellery.Utils.StaticVariables.TG_GROUP_ID;

@Service
@RequiredArgsConstructor
public class ParticipantsService {
    
    private final ParticipantsMessagesRepository participantsRepository;
    private final MainTelegramBot telegramBot;

    //------------------------------------CRUD-----------------------------------------//

    public String participantRegistration(ParticipantsDTO participantsDTO) {

        ParticipantsMessages participant = new ParticipantsMessages();

        participant.setFullName(participantsDTO.getFullName());
        participant.setEmail(participantsDTO.getEmail());
        participant.setMessage(participantsDTO.getMessage());
        participant.setCreationTime(LocalDateTime.now());

        participantsRepository.save(participant);

        SendMessage message = new SendMessage();

        message.setChatId(TG_GROUP_ID);
        message.setText("New Message Received:\n--------------------------------\n\nFull Name: "
                + participant.getFullName() + "\nEmail: " + participant.getEmail()
                + "\nMessage: " + participant.getMessage());

        telegramBot.sendMessage(message);

        return "You Successfully Registered!";
    }

    public String updateParticipant(Long id, ParticipantsDTO participantsDTO) {

        ParticipantsMessages participant = participantsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        if (participantsDTO.getEmail() != null) {
            participant.setEmail(participantsDTO.getEmail());
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

        ParticipantsMessages participant = participantsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

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