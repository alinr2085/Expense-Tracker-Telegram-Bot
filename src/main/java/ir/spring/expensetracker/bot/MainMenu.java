package ir.spring.expensetracker.bot;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {

    public static ReplyKeyboardMarkup getMenu() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("افزودن ➕"));
        row.add(new KeyboardButton("تاریخچه \uD83D\uDCDC"));
        row.add(new KeyboardButton("بیخیال ❌"));

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(rows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(false);

        return keyboardMarkup;
    }


}
