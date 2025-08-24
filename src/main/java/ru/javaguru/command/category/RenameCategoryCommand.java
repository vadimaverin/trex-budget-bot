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
public class RenameCategoryCommand implements Command {

    private final TelegramClient client;
    private final UserStateService stateService;

    @Override
    public BotCommand command() {
        return BotCommand.RENAMECATEGORY;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text("Укажите новое имя для категории:")
                .build();
        try {
            client.execute(msg);
            stateService.setUserState(chatId,
                    UserState.WAITING_NAME_UPDATE_CATEGORY,
                    commonInfo.getUpdateId());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
