package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.CalendarKeyboard;
import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class DateStep implements ConversationStepHandler {


    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;


    public DateStep(SessionManager sessionManager, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public ConversationState getConversationState() {
        return ConversationState.DATE;
    }

    @Override
    public void handle(Update update, UserSession userSession) {
        if (update.hasMessage()) {
            displayMessage.displayKeyboardMessage(
                    update.getMessage().getChatId(),
                    "انتخاب کن دیگه",
                    CalendarKeyboard.build(YearMonth.now())
            );
        }
        else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.startsWith(CalendarKeyboard.DAY_PREFIX)) {
                String dateStr = callbackData.substring(CalendarKeyboard.DAY_PREFIX.length());
                LocalDate date = LocalDate.parse(dateStr);

                userSession.getMaintainingDraft().setTransactionDate(date);

                sessionManager.updateState(update.getCallbackQuery().getFrom().getId(), ConversationState.DESCRIPTION);

                displayMessage.displayMessage(chatId, "چیر دیگه ای برای گفتن داری در مورد این تراکنش ؟ بگو");
            }
            else if (callbackData.startsWith(CalendarKeyboard.NAV_PREFIX)) {
                String monthStr = callbackData.substring(CalendarKeyboard.NAV_PREFIX.length());
                YearMonth newMonth = YearMonth.parse(monthStr);

                displayMessage.displayKeyboardMessage(
                        chatId,
                        "تقویم آپدیت شد، حالا انتخاب کن:",
                        CalendarKeyboard.build(newMonth)
                );
            }
        }
    }
}
