package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.MaintainingDraft;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.entity.Transaction;
import ir.spring.expensetracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class SelectTransactionStep implements ConversationStepHandler {
    private final TransactionRepository transactionRepository;
    private final DisplayMessage displayMessage;
    private final SessionManager sessionManager;

    public SelectTransactionStep(TransactionRepository transactionRepository, DisplayMessage displayMessage, SessionManager sessionManager) {
        this.transactionRepository = transactionRepository;
        this.displayMessage = displayMessage;
        this.sessionManager = sessionManager;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.SELECT_TRANSACTION;
    }

    @Override
    @Transactional
    public void handle(Update update, UserSession userSession) {
        try {
            int index = Integer.parseInt(update.getMessage().getText()) - 1;
            Long telegramId = update.getMessage().getFrom().getId();

            System.out.println(index);
            List<Transaction> transactions = transactionRepository
                    .findByUser_TelegramIdOrderByDateDesc(telegramId).stream().limit(10).toList();

            if (index >= 0 && index < transactions.size()) {
                if (userSession.getMaintainingDraft() == null) {
                    userSession.setMaintainingDraft(new MaintainingDraft());
                }
                userSession.getMaintainingDraft().setTransactionId(transactions.get(index).getId());

                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton("ویرایش ✏\uFE0F"));
                row.add(new KeyboardButton("پاک کردن \uD83D\uDDD1\uFE0F"));
                row.add(new KeyboardButton("بیخیال ❌"));
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row));
                keyboardMarkup.setResizeKeyboard(true);

                displayMessage.displayKeyboardMessage(update.getMessage().getChatId(), "خب، حالا چیکارش کنم؟ ویرایش یا حذف؟", keyboardMarkup);
                sessionManager.updateState(telegramId, ConversationState.IDLE);

            } else {
                displayMessage.displayMessage(update.getMessage().getChatId(), "اصلا همچین تراکنشی داری بنظرت؟ یه عدد بین 1  تا " + transactions.size() + " بگو.");
            }
        } catch (Exception e) {
            displayMessage.displayMessage(update.getMessage().getChatId(), "باید عدد وارد کنی 😐");
        }
    }
}
