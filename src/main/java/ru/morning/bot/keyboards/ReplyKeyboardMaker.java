package ru.morning.bot.keyboards;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.morning.bot.files.Reply;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Reply.JOKE.getText()));
        //KeyboardRow row2 = new KeyboardRow();
        //row2.add(new KeyboardButton(Reply.NEWS.getText()));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        //keyboard.add(row2);
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
