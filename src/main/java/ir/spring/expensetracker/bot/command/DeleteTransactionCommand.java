package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.MaintainingDraft;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeleteTransactionCommand implements CommandHandler {

    private final SessionManager sessionManager;
    private final TransactionRepository transactionRepository;
    private final DisplayMessage displayMessage;



    public DeleteTransactionCommand(SessionManager sessionManager, TransactionRepository transactionRepository, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.transactionRepository = transactionRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCommandName() {
        return "پاک کردن \uD83D\uDDD1\uFE0F";
    }

    @Override
    public void handleCommand(Update update) {
        UserSession userSession = sessionManager.getUserSession(update.getMessage().getFrom().getId());
        MaintainingDraft draft = userSession.getMaintainingDraft();
        Long transactionId = draft != null ? draft.getTransactionId() : null;

        if (transactionId != null) {
            transactionRepository.deleteById(transactionId);
            displayMessage.displayMessage(update.getMessage().getChatId(), "تراکنش مورد نظر پاک شد");
            sessionManager.clearSession(update.getMessage().getFrom().getId());
        }
        else {
            displayMessage.displayMessage(update.getMessage().getChatId(), "در خواستت برای این بخش نیست \uD83D\uDE10 اول باید یه تراکنش انتخاب کنی. برو تو تاریخچت");
        }
    }
}
