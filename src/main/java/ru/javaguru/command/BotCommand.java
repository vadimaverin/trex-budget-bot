package ru.javaguru.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotCommand {

    START("/start"),
    HELP("/help"),
    ADDEXPENSE("/addExpense"),
    LISTEXPENSE("/listExpense"),
    CATEGORIES("/listCategories"),
    RENAMECATEGORY("/renameCategory"),
    ADDCATEGORY("/addCategory"),
    FILLCATEGORYALIAS("/fillCategoryAlias"),
    LISTCURRENTEXPENSE("/listCurrentExpense"),
    DETALIEDRECORDSFORDAY("/detailedRecordsForDay"),
    DETALIEDRECORDSFORWEEK("/detailedRecordsForWeek"),
    DETALIEDRECORDSFORMONTH("/detailedRecordsForMonth");

    private final String command;

    public static BotCommand fromString(String command) {
        for (var userCommand : BotCommand.values()) {
            if (userCommand.getCommand().equals(command)) {
                return userCommand;
            }
        }
        return null;
    }
}
