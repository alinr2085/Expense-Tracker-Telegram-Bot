package ir.spring.expensetracker.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendarKeyboard {

    public static final String PREFIX = "cal:";
    public static final String DAY_PREFIX = PREFIX + "day:";
    public static final String NAV_PREFIX = PREFIX + "nav:";
    public static final String IGNORE = PREFIX + "ignore";

    public static InlineKeyboardMarkup build(YearMonth yearMonth) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        InlineKeyboardRow header = new InlineKeyboardRow();
        header.add(navButton("«", yearMonth.minusMonths(1)));
        header.add(InlineKeyboardButton.builder()
                .text(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, new Locale("fa")) + " " + yearMonth.getYear())
                .callbackData(IGNORE)
                .build());
        header.add(navButton("»", yearMonth.plusMonths(1)));
        rows.add(header);

        int daysInMonth = yearMonth.lengthOfMonth();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            currentRow.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(day))
                    .callbackData(DAY_PREFIX + date)
                    .build());

            if (currentRow.size() == 7) {
                rows.add(currentRow);
                currentRow = new InlineKeyboardRow();
            }
        }
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private static InlineKeyboardButton navButton(String label, YearMonth targetMonth) {
        return InlineKeyboardButton.builder()
                .text(label)
                .callbackData(NAV_PREFIX + targetMonth)
                .build();
    }
}