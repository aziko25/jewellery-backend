package jewellery.jewellery.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jewellery.jewellery.Models.ParticipantsMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    public ParticipantsDTO(ParticipantsMessages participant) {

        setId(participant.getId());
        setFullName(participant.getFullName());
        setEmail(participant.getEmail());
        setPhone(participant.getPhone());
        setMessage(participant.getMessage());
    }
}