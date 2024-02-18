package jewellery.jewellery.Controllers.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jewellery.jewellery.MainTelegramBot;
import jewellery.jewellery.Models.Participants;
import jewellery.jewellery.Repositories.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static jewellery.jewellery.Utils.StaticVariables.DOCUMENTS_SAVING_DIR;
import static jewellery.jewellery.Utils.StaticVariables.TG_GROUP_ID;

@RestController
@RequestMapping("/api")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class SignupController {

    private final ParticipantsRepository participantsRepository;
    private final MainTelegramBot telegramBot;

    @PostMapping("/downloadDocument/{fileName}")
    public ResponseEntity<Resource> fetchFile(@PathVariable String fileName) {

        // Construct the full path to the file
        String fullPath = DOCUMENTS_SAVING_DIR + "/" + fileName;
        Resource resource = new FileSystemResource(fullPath);

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException("Error: File not found or not readable");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/downloadFile")
    public ResponseEntity<Resource> fetchFile() throws UnsupportedEncodingException {

        String fileName = "Заявка_на_участие_в_выставке.docx";
        Resource resource = new ClassPathResource(fileName);

        String encodedFilename = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8.toString());
        String asciiFilename = "Zayavka_na_uchastie_v_vystavke.docx"; // ASCII approximation of the filename

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + asciiFilename + "\"; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestParam("participant") String participantJson,
                                    @RequestParam(name = "file", required = false) MultipartFile file) throws IOException, TelegramApiException {

        Participants participant = new ObjectMapper().readValue(participantJson, Participants.class);

        if (participant.getUserType() == null || participant.getUserType().isEmpty()) {
            throw new IllegalArgumentException("User Type Is Empty!");
        }

        String userType = participant.getUserType();

        if (userType.isEmpty() || (!userType.equalsIgnoreCase("ATTENDANT") && !userType.equalsIgnoreCase("PARTICIPANT"))) {
            throw new IllegalArgumentException("User Type Must Be Attendant Or Participant!");
        }

        Participants participants = new Participants();

        participants.setEmail(participant.getEmail());
        participants.setPhone(participant.getPhone());
        participants.setFullName(participant.getFullName());
        participants.setCountry(participant.getCountry());
        participants.setOrganization(participant.getOrganization());
        participants.setUserType(userType);
        participants.setRegistrationTime(LocalDateTime.now());

        if (userType.equalsIgnoreCase("PARTICIPANT")) {

            if (file == null || file.isEmpty()) {

                throw new IllegalArgumentException("Upload File!");
            }

            String baseName = participant.getFullName().replace(" ", "_");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
            String dateTime = now.format(formatter);

            String filename = baseName + "_" + dateTime;

            String originalFileExtension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            String fullFilename = filename + originalFileExtension;

            String directoryPath = DOCUMENTS_SAVING_DIR;

            Path filePath = Paths.get(directoryPath, fullFilename);

            participants.setDocLink(fullFilename);

            try {
                file.transferTo(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            participants.setSpace(participant.getSpace());

            String extension = "\nSpace: " + participant.getSpace();

            String originalFilename = file.getOriginalFilename();

            if (originalFilename != null && (originalFilename.endsWith(".pdf") || originalFilename.endsWith(".docx") || originalFilename.endsWith(".word"))) {

                String tempDirectoryPath = System.getProperty("java.io.tmpdir");
                Path tempFilePath = Paths.get(tempDirectoryPath, fullFilename);
                file.transferTo(tempFilePath.toFile());

                File tempFile = tempFilePath.toFile();
                InputFile inputFile = new InputFile(tempFile);

                try {

                    SendDocument sendDocument = new SendDocument();

                    sendDocument.setChatId(TG_GROUP_ID);
                    sendDocument.setDocument(inputFile);
                    sendDocument.setCaption("Новый Участник Зарегестрировался:\n--------------------------------\n\n" +
                            "Email: " + participant.getEmail() +
                            "\nPhone: " + participant.getPhone() +
                            "\nCountry: " + participant.getCountry() +
                            "\nOrganization: " + participant.getOrganization() +
                            "\nFull Name: " + participant.getFullName() +
                            extension);

                    telegramBot.execute(sendDocument);
                }
                catch (TelegramApiRequestException ignored) {}
            }
            else {

                throw new IllegalArgumentException("File must be a .pdf, .docx or .word file!");
            }
        }
        else {

            SendMessage message = new SendMessage();

            message.setChatId(TG_GROUP_ID);
            message.setText("Новый Посетитель Зарегестрировался:\n--------------------------------\n\n" +
                    "Email: " + participant.getEmail() +
                    "\nPhone: " + participant.getPhone() +
                    "\nCountry: " + participant.getCountry() +
                    "\nOrganization: " + participant.getOrganization() +
                    "\nFull Name: " + participant.getFullName());

            telegramBot.sendMessage(message);
        }

        participantsRepository.save(participants);

        return ResponseEntity.ok("You Successfully Signed Up!");
    }
}