package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.MaintainingDraft;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.entity.Transaction;
import ir.spring.expensetracker.repository.CategoryRepository;
import ir.spring.expensetracker.repository.TransactionRepository;
import ir.spring.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

@Component
public class DescriptionStep implements ConversationStepHandler {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public DescriptionStep(UserRepository userRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository, SessionManager sessionManager, DisplayMessage displayMessage) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.DESCRIPTION;
    }

    @Override
    public void handle(Update update, UserSession userSession) {
        userSession.getMaintainingDraft().setDescription(update.getMessage().getText());

        Transaction newTransaction = new Transaction();
        newTransaction.setCategory(categoryRepository.findById(userSession.getMaintainingDraft().getCategoryId()).orElseThrow());
        newTransaction.setDescription(userSession.getMaintainingDraft().getDescription());
        newTransaction.setAmount(userSession.getMaintainingDraft().getAmount());
        newTransaction.setType(userSession.getMaintainingDraft().getTransactionType());
        newTransaction.setDate(userSession.getMaintainingDraft().getTransactionDate());
        newTransaction.setUser(userRepository.findByTelegramId(update.getMessage().getFrom().getId()).orElseThrow());
        transactionRepository.save(newTransaction);
        sessionManager.clearSession(update.getMessage().getFrom().getId());
        displayMessage.displayMessage(update.getMessage().getChatId(), "اطلاعاتت ذخیره شد \uD83D\uDC4D");
    }
}
