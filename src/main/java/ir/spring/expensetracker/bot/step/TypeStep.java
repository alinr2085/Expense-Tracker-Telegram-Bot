package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.entity.TransactionType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TypeStep implements ConversationStepHandler {


    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public TypeStep(SessionManager sessionManager, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }


    @Override
    public ConversationState getConversationState() {
        return ConversationState.TYPE;
    }

    @Override
    public void handle(Update update, UserSession userSession) {

        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackQuery = update.getCallbackQuery().getData();
            TransactionType transactionType = callbackQuery.equals("Expense") ? TransactionType.Expense : TransactionType.Income;
            userSession.getMaintainingDraft().setTransactionType(transactionType);
            sessionManager.updateState(chatId, ConversationState.CATEGORY);

            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(categories(transactionType));
            displayMessage.displayKeyboardMessage(
                    chatId,
                    "هزینه مورد نظر در چه زمینه ای بوده؟ یکی از اینارو بگو:\n",
                    keyboardMarkup
            );
        } else if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            displayMessage.displayMessage(chatId, "حواست کجاست \uD83D\uDE10 ؟");
        }
    }


    private List<InlineKeyboardRow> categories(TransactionType type) {
        InlineKeyboardRow row = new InlineKeyboardRow();

        if (type == TransactionType.Expense) {
            InlineKeyboardButton invoices = new InlineKeyboardButton("صورت حساب");
            invoices.setCallbackData("Invoices");
            InlineKeyboardButton transportation = new InlineKeyboardButton("حمل و نقل");
            transportation.setCallbackData("Transportation");
            InlineKeyboardButton feed = new InlineKeyboardButton("غذا");
            feed.setCallbackData("Feed");
            InlineKeyboardButton shop = new InlineKeyboardButton("فروشگاه");
            shop.setCallbackData("Shop");
            row.add(invoices);
            row.add(transportation);
            row.add(feed);
            row.add(shop);
        } else if (type == TransactionType.Income) {
            InlineKeyboardButton profit = new InlineKeyboardButton("سود");
            profit.setCallbackData("Profit");
            InlineKeyboardButton salary = new InlineKeyboardButton("دستمزد");
            salary.setCallbackData("Salary");
            InlineKeyboardButton gifts = new InlineKeyboardButton("هدیه");
            gifts.setCallbackData("Gifts");
            row.add(profit);
            row.add(salary);
            row.add(gifts);

        }
        return List.of(row);
    }
}


