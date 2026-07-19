package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import java.math.BigDecimal;
import java.util.List;


@Component
public class AmountStep implements ConversationStepHandler {

    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public AmountStep(SessionManager sessionManager, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.AMOUNT;
    }

    @Override
    public void handle(Update update, UserSession userSession) {
        try {
            BigDecimal amount = new BigDecimal(update.getMessage().getText());
            userSession.getMaintainingDraft().setAmount(amount);
            sessionManager.updateState(update.getMessage().getFrom().getId(), ConversationState.TYPE);
            InlineKeyboardButton expenseBtn = new InlineKeyboardButton("بگا دادم 📉");
            expenseBtn.setCallbackData("Expense");
            InlineKeyboardButton incomeBtn = new InlineKeyboardButton("در آوردم 📈");
            incomeBtn.setCallbackData("Income");
            InlineKeyboardRow row = new InlineKeyboardRow();
            row.add(expenseBtn);
            row.add(incomeBtn);
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(List.of(row));
            displayMessage.displayKeyboardMessage(
                    update.getMessage().getChatId(),
                    "خب، اول بگو ببینم پولت رو خرج کردی یا پول دراوردی؟",
                    keyboardMarkup
            );
        } catch (Exception e) {
            displayMessage.displayMessage(update.getMessage().getChatId(), "باید عدد وارد کنی \uD83D\uDE10");
            displayMessage.displayMessage(update.getMessage().getChatId(), e.getMessage());
        }
    }
}
