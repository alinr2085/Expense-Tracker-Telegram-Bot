package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.CalendarKeyboard;
import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.entity.Category;
import ir.spring.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.YearMonth;

@Component
public class CategoryStep implements ConversationStepHandler {

    private final CategoryRepository categoryRepository;
    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public CategoryStep(CategoryRepository categoryRepository, SessionManager sessionManager, DisplayMessage displayMessage) {
        this.categoryRepository = categoryRepository;
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.CATEGORY;
    }

    @Override
    public void handle(Update update, UserSession userSession) {

        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackQuery = update.getCallbackQuery().getData();
            Category category = categoryRepository.findByName(callbackQuery);
            try {
                userSession.getMaintainingDraft().setCategoryId(category.getId());
                sessionManager.updateState(chatId, ConversationState.DATE);
                displayMessage.displayKeyboardMessage(
                        chatId,
                        "مال چه تاریخیه",
                        CalendarKeyboard.build(YearMonth.now())
                );
            } catch (Exception e) {
                displayMessage.displayMessage(update.getMessage().getChatId(), "ریدی \uD83D\uDE10");
            }
        } else if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            displayMessage.displayMessage(chatId, "حواست کجاست \uD83D\uDE10 ؟");
        }
    }
}
