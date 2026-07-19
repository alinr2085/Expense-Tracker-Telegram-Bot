package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.CalendarKeyboard;
import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.command.HistoryCommand;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.MaintainingDraft;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.entity.Transaction;
import ir.spring.expensetracker.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class EditTransactionStep implements ConversationStepHandler {
    private final DisplayMessage displayMessage;
    private final TransactionRepository transactionRepository;
    private final SessionManager sessionManager;
    private final HistoryCommand historyCommand;

    public EditTransactionStep(DisplayMessage displayMessage, TransactionRepository transactionRepository, SessionManager sessionManager, HistoryCommand historyCommand) {
        this.displayMessage = displayMessage;
        this.transactionRepository = transactionRepository;
        this.sessionManager = sessionManager;
        this.historyCommand = historyCommand;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.EDIT_MODE;
    }

    @Override
    public void handle(Update update, UserSession userSession) {
        MaintainingDraft draft = userSession.getMaintainingDraft();

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            draft.setEditField(callbackData);
            String displayText = callbackData.equals("amount") ? "مبلغ جدید رو بفرست" : "توضیحات جدید رو بفرست";
            displayMessage.displayMessage(update.getCallbackQuery().getMessage().getChatId(), displayText);
        } else if (update.hasMessage() && draft.getEditField() != null) {
            Long chatId = update.getMessage().getChatId();
            try {
                Transaction transaction = transactionRepository.findById(draft.getTransactionId()).orElseThrow();
                if (draft.getEditField().equals("amount")) {
                    transaction.setAmount(new BigDecimal(update.getMessage().getText()));
                } else if (draft.getEditField().equals("description")) {
                    transaction.setDescription(update.getMessage().getText());
                }
                transactionRepository.save(transaction);
                sessionManager.clearSession(update.getMessage().getFrom().getId());
                displayMessage.displayMessage(chatId, "بخش مورد نظرت اپدیت شد ✅");
                historyCommand.handleCommand(update);

            } catch (Exception e) {
                displayMessage.displayMessage(chatId, "یه جایی اشتباه کردی درستش کن");
            }
        }else {
            displayMessage.displayMessage(update.getMessage().getChatId(), "حواست کجاست \uD83D\uDE10 ؟");

        }
    }
}
