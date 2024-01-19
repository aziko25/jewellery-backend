package jewellery.jewellery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MainTelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

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

    public MainTelegramBot(@Value("${bot.token}") String botToken) {

        super(botToken);
    }

    @Override
    public String getBotUsername() {

        return "uz_jewellery_bot";
    }
}