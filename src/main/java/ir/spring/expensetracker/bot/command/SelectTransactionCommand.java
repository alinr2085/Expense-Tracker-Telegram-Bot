package ir.spring.expensetracker.bot.command;


import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class SelectTransactionCommand implements CommandHandler {

    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public SelectTransactionCommand(DisplayMessage displayMessage, SessionManager sessionManager) {
        this.displayMessage = displayMessage;
        this.sessionManager = sessionManager;
    }

    @Override
    public String getCommandName() {
        return "انتخاب تراکنش \uD83D\uDCCC";
    }

    @Override
    public void handleCommand(Update update) {
        displayMessage.displayMessage(update.getMessage().getChatId(), "شماره هر کدومو میخوای بگو");
        sessionManager.updateState(update.getMessage().getChatId(), ConversationState.SELECT_TRANSACTION);
    }
}
