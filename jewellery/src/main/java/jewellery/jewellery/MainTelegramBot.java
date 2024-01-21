package jewellery.jewellery;

import jewellery.jewellery.Models.Participants;
import jewellery.jewellery.Models.Users;
import jewellery.jewellery.Repositories.ParticipantsRepository;
import jewellery.jewellery.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class MainTelegramBot extends TelegramLongPollingBot {

    private final ParticipantsRepository repository;
    private final UsersRepository usersRepository;

    public MainTelegramBot(@Value("${bot.token}") String botToken, ParticipantsRepository repository, UsersRepository usersRepository) {

        super(botToken);
        this.repository = repository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();

            Long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start")) {

                Users user = usersRepository.findByTelegramId(chat_id);

                if (user != null) {

                    List<Participants> participants = repository.findAll(Sort.by("creationTime"));

                    StringBuilder response = new StringBuilder();
                    int count = 0;

                    for (Participants participant : participants) {

                        response.append("\n\nEmail: ").append(participant.getEmail())
                                .append("\nFull Name: ").append(participant.getFullName())
                                .append("\nMessage: ").append(participant.getMessage())
                                .append("\n-------------");

                        count++;

                        if (count % 3 == 0) {

                            SendMessage message = new SendMessage();

                            message.setChatId(String.valueOf(chat_id));
                            message.setText(response.toString());
                            sendMessage(message);

                            response = new StringBuilder();
                        }
                    }

                    if (response.length() > 0) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chat_id));
                        message.setText(response.toString());
                        sendMessage(message);
                    }
                }
            }
        }
    }


    public void sendMessage(SendMessage message) {

        try {

            execute(message);
        }
        catch (TelegramApiException e) {

            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {

        return "uz_jewellery_bot";
    }
}