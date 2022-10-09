package ru.morning.bot.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardUserResponse {
    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        InlineKeyboardButton noButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        yesButton.setCallbackData("good_joke");
        noButton.setText("Нет");
        noButton.setCallbackData("bad_joke");
        row.add(yesButton);
        row.add(noButton);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public InlineKeyboardMarkup getSurveyKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton type1 = new InlineKeyboardButton();
        InlineKeyboardButton type2 = new InlineKeyboardButton();
        type1.setText("Анекдот.ру");
        type1.setCallbackData("type_1");
        type2.setText("АнекдотБар.ру");
        type2.setCallbackData("type_2");
        row1.add(type1);
        row2.add(type2);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        markup.setKeyboard(keyboard);
        return markup;
    }
}