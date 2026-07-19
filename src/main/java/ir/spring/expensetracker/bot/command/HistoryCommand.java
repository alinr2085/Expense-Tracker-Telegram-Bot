package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.MainMenu;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.entity.Transaction;
import ir.spring.expensetracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryCommand implements CommandHandler {

    private final TransactionRepository transactionRepository;
    private final DisplayMessage displayMessage;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private final SessionManager sessionManager;

    public HistoryCommand(TransactionRepository transactionRepository, DisplayMessage displayMessage, SessionManager sessionManager) {
        this.transactionRepository = transactionRepository;
        this.displayMessage = displayMessage;
        this.sessionManager = sessionManager;
    }


    @Override
    public String getCommandName() {
        return "تاریخچه \uD83D\uDCDC";
    }

    @Override
    @Transactional
    public void handleCommand(Update update) {
        sessionManager.clearSession(update.getMessage().getFrom().getId());
        List<Transaction> transactions = transactionRepository.
                findByUser_TelegramIdOrderByDateDesc(update.getMessage().getFrom().getId()).stream().limit(10).toList();
        if (transactions.isEmpty()) {
            displayMessage.displayMessage(update.getMessage().getChatId(), "تا الان گوهی نخوردی.\nمیتونی بری یه گوهی بخوری و ثبتش کنی بعد بیای اینجا تا نشونت بدم.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("تاریخچه 10 کون اخری که دادی:\n\n");
        for (int i = 0; i < transactions.size(); i++) {
            stringBuilder.append(i + 1).append(":\n");
            stringBuilder.append(DATE_FORMAT.format(transactions.get(i).getDate())).append("\n");
            stringBuilder.append(transactions.get(i).getType()).append("\n");
            stringBuilder.append(transactions.get(i).getCategory().getName()).append("\n");
            stringBuilder.append(transactions.get(i).getAmount()).append("\n");
            stringBuilder.append(transactions.get(i).getDescription()).append("\n");

            stringBuilder.append("______________\n\n");
        }

        displayMessage.displayMessage(update.getMessage().getChatId(), stringBuilder.toString());
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("دانلود فایل \uD83D\uDDC2\uFE0F"));
        row.add(new KeyboardButton("انتخاب تراکنش \uD83D\uDCCC"));
        row.add(new KeyboardButton("بیخیال ❌"));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row));
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(false);
        displayMessage.displayKeyboardMessage(update.getMessage().getChatId(),
                "میتونی فایل تمام تراکنش هاتو دریافت کنی یا هر کدومو میخوای ویرایش یا حذف کنی روش کلیک کن یا که بیخیال شو و برگرد منواصلی",
                keyboardMarkup);

    }
}
