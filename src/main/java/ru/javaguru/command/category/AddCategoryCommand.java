package ru.javaguru.command.category;

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
public class AddCategoryCommand implements Command {

    private final UserStateService userStateService;
    private final TelegramClient client;


    @Override
    public BotCommand command() {
        return BotCommand.ADDCATEGORY;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Укажите имя новой категории:")
                .build();

        try {
            client.execute(message);
            userStateService.setUserState(chatId, UserState.WAITING_NAME_ADD_CATEGORY);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
