package ru.javaguru.command;

import ru.javaguru.dto.CommonInfo;
import ru.javaguru.service.UserService;
import ru.javaguru.service.UserStateService;
import ru.javaguru.utils.InlineKeyboardBuilder;
import ru.javaguru.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardStartCommand;

@Service
@RequiredArgsConstructor
public class StartCommand implements Command{

    private final TelegramClient client;
    private final UserService userService;
    private final UserStateService userStateService;

    public BotCommand command() {
        return BotCommand.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        String messageText = """
                Привет!👋
                Я бот для учета расходов 🤑
                Вот что я умею:
                
                * 📋 Добавлять и изменять категории /listCategories
                * 💸 Добавлять расход /addExpense
                * 📊 Выводить статистику по расходам
                    - /listCurrentExpense
                    - /detailedRecordsForDay, итоги по категориям за день.
                    - /detailedRecordsForWeek, итоги по категориям за неделю.
                    - /detailedRecordsForDayMonth, итоги по категориям за месяц.
                
                Воспользуйтесь командой /help для справки
                """;
        Long chatId = commonInfo.getChatId();
        String userName = commonInfo.getUserFromTelegram().getUserName();
        userService.findOrCreateUser(chatId, userName);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(getInlineKeyboardStartCommand())
                .build();

        try {
            client.execute(sendMessage);
            userStateService.clearUserState(chatId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
