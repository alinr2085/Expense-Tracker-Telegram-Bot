package ir.spring.expensetracker.bot;


import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;

@Component
public class DisplayMessage {

    private final TelegramClient client;

    public DisplayMessage(TelegramClient client) {
        this.client = client;
    }

    public void displayMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(message).build();

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void displayKeyboardMessage(Long chatId, String message, ReplyKeyboard keyboard) {
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(message).replyMarkup(keyboard).build();

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerCallback(String callbackQueryId) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build();
        try {
            client.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(Long chatId, String caption, InputStream file, String filename) {
        SendDocument sendDocument = SendDocument.builder().chatId(chatId).
                caption(caption).document(new InputFile(file, filename)).build();
        try {
            client.execute(sendDocument);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
