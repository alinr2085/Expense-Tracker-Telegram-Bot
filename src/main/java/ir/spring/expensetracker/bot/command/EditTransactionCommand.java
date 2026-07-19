package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class EditTransactionCommand implements CommandHandler {

    private final DisplayMessage displayMessage;
    private final SessionManager sessionManager;

    public EditTransactionCommand(DisplayMessage displayMessage, SessionManager sessionManager) {
        this.displayMessage = displayMessage;
        this.sessionManager = sessionManager;
    }

    @Override
    public String getCommandName() {
        return "ویرایش ✏\uFE0F";
    }

    @Override
    public void handleCommand(Update update) {
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(InlineKeyboardButton.builder().text("مبلغ").callbackData("amount").build());
        row.add(InlineKeyboardButton.builder().text("توضیحات").callbackData("description").build());
        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder().keyboardRow(row).build();

        displayMessage.displayKeyboardMessage(update.getMessage().getChatId(), "چیشو میخوای تغییر بدی ؟", keyboard);
        sessionManager.updateState(update.getMessage().getFrom().getId(), ConversationState.EDIT_MODE);
    }
}
