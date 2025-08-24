package ru.javaguru.command;

import ru.javaguru.dto.CommonInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final TelegramClient client;

    public BotCommand command() {
        return BotCommand.HELP;
    }

    public void execute(CommonInfo commonInfo) {

        String messageText = """
                * /start, начало работы и стартовое меню.
                    Содержит краткое описание и набор команд.
                * /addCategory, добавление новой категории.
                    После добавления, будет предложено ввести
                    псевдоним для категории.
                    Псевдоним это короткое название категории.
                    Например: для категории Продукты, псевдоним: еда
                    Псевдоним можно не заполнять, но тогда при добавлении
                    нового расхода нужно ввести точное имя категории с
                    учетом регистра.
                    Допустимо:
                        Для имени любой набор из символов
                        с максимальной длиной 255 символов
                        Для псевдонима любой набор из символов
                        без пробелов
                * /listCategories, Вывод списка категорий.
                    Переименовать категорию можно через вывод
                    всех категорий.
                    После переименования категории будет предложено
                    заполнить псевдоним.
                * /addExpense, добавление расхода
                    - Формат {Сумма} {Категория} [Описание]
                    - {Сумма} Обязательно для заполнения
                        Допустимо:
                            - 100
                            - 100.99
                            - 100,99
                    - {Категория} Обязательно для заполнения
                        Допустимо:
                            Полное имя категории с учетом регистра, например "Продукты"
                            Псевдоним категории, если заполнен, например "еда"
                   - [Описание] Не обязательно для заполнения
                        Допустимо:
                            Любой набор из символов
                            с максимальной длиной 255 символов
                * /listCurrentExpense
                    Вывод списка итогов без категорий.
                    за день, неделю, месяц.
                * /detailedRecordsForDay
                    Вывод итогов по категориям за текущий день.
                    Сортировка по убыванию.
                * /detailedRecordsForWeek
                    Вывод итогов по категориям за текущую неделю.
                    Сортировка по убыванию.
                * /detailedRecordsForMonth
                    Вывод итогов по категориям за текущий месяц.
                    Сортировка по убыванию.
                """;

        SendMessage message = SendMessage.builder()
                .chatId(commonInfo.getChatId())
                .text(messageText)
                .build();
        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}
