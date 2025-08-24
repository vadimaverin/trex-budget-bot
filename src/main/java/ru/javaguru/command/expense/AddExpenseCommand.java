package ru.javaguru.command.expense;


import ru.javaguru.command.BotCommand;
import ru.javaguru.command.Command;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.service.UserStateService;
import ru.javaguru.states.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class AddExpenseCommand implements Command {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    @Override
    public BotCommand command() {
        return BotCommand.ADDEXPENSE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(commonInfo.getChatId())
                .text("Введите расход:")
                .build();

        userStateService.setUserState(chatId, UserState.WAITING_EXPENSE);

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
