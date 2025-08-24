package ru.javaguru.utils;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String[] parseUpdateMassage(String textMessage) {

        //Разделяет строку в местах, где за не-цифрой (\D) следует цифра (\d).
        return textMessage.split("(?<=\\D)(?=\\d)");

    }
}
