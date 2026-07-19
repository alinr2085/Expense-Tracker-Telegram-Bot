package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.MainMenu;
import ir.spring.expensetracker.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class StartCommand implements CommandHandler {

    private final UserService userService;
    private final DisplayMessage displayMessage;

    public StartCommand(UserService userService, DisplayMessage displayMessage) {
        this.userService = userService;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public void handleCommand(Update update) {
        User user = update.getMessage().getFrom();
        userService.findOrCreateUser(update.getMessage().getFrom().getId(),
                user.getUserName(), user.getFirstName(), user.getLastName());

        displayMessage.displayKeyboardMessage(update.getMessage().getChatId(), "سلام خوش اومدی به ربات مدیریت کیف پول اینجا هر هزینه ای رو میتونی ثبت کنی \uD83D\uDC4B\n میتونی با دکمه های زیر شروع کنی :",
                MainMenu.getMenu());
    }
}
