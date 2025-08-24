package ru.javaguru.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {

    public static InlineKeyboardMarkup getInlineKeyboardGoToStartCommand() {

        var button1 = goToStartButton();

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(createRow(button1));

        return new InlineKeyboardMarkup(rows);

    }

    public static InlineKeyboardMarkup getInlineKeyboardStartCommand() {

        var button1 = addCategoriesButton();
        var button2 = addExpenseButton();
        var button3 = listCategoriesButton();

        var button4 = listCurrentExpense();
        var button5 = listDetailedRecordsForDay();
        var button6 = listDetailedRecordsForWeek();
        var button7 = listDetailedRecordsForMonth();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button1);
        row.add(button2);

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(row);
        rows.add(createRow(button3));

        rows.add(createRow(button4));
        rows.add(createRow(button5));
        rows.add(createRow(button6));
        rows.add(createRow(button7));

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkupFillAlias() {

        var button1 = fillCategoryAliasYesButton();
        var button3 = goToStartButton();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button1);

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(row);
        rows.add(createRow(button3));

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkupAddCategories() {

        var button1 = addCategoriesButton();
        var button2 = goToStartButton();

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(createRow(button1));
        rows.add(createRow(button2));

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkupDetailedExpenses() {

        var button1 = listDetailedRecordsForDay();
        var button2 = listDetailedRecordsForWeek();
        var button3 = listDetailedRecordsForMonth();
        var button4 = goToStartButton();

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(createRow(button1));
        rows.add(createRow(button2));
        rows.add(createRow(button3));
        rows.add(createRow(button4));

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkupAddExpense() {

        var button1 = addExpenseButton();
        var button2 = goToStartButton();

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(createRow(button1));
        rows.add(createRow(button2));

        return new InlineKeyboardMarkup(rows);

    }

    public static InlineKeyboardButton listCategoriesButton() {
        return createButton("\uD83D\uDCCB Вывести категории расходов", "/listCategories");
    }

    public static InlineKeyboardButton addCategoriesButton() {
        return createButton("💾 Добавить категорию", "/addCategory");
    }

    public static InlineKeyboardButton addExpenseButton() {
        return createButton("\uD83D\uDCB8 Добавить расход", "/addExpense");
    }

    public static InlineKeyboardButton fillCategoryAliasYesButton() {
        return createButton("Да", "/fillCategoryAlias");
    }

    public static InlineKeyboardButton goToStartButton() {
        return createButton("⬅\uFE0F Вернуться в стартовое меню", "/start");
    }

    public static InlineKeyboardButton listCurrentExpense() {
        return createButton("Текущие расходы", "/listCurrentExpense");
    }

    public static InlineKeyboardButton listDetailedRecordsForDay() {
        return createButton("за ДЕНЬ", "/detailedRecordsForDay");
    }

    public static InlineKeyboardButton listDetailedRecordsForWeek() {
        return createButton("за НЕДЕЛЮ", "/detailedRecordsForWeek");
    }

    public static InlineKeyboardButton listDetailedRecordsForMonth() {
        return createButton("за МЕСЯЦ", "/detailedRecordsForMonth");
    }

    private static InlineKeyboardButton createButton(String text, String callbackData) {
        var button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private static InlineKeyboardRow createRow(InlineKeyboardButton button) {
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(button);
        return row;
    }

}
