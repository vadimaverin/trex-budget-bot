package ru.javaguru.command.category;

import ru.javaguru.command.BotCommand;
import ru.javaguru.command.Command;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.dto.UserStateDto;
import ru.javaguru.service.UserStateService;
import ru.javaguru.states.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FillCategoryAliasCommand implements Command {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    @Override
    public BotCommand command() {
        return BotCommand.FILLCATEGORYALIAS;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        long chatId = commonInfo.getChatId();
        userStateService.getUserStateDto(chatId)
                .ifPresentOrElse(
                        userState -> sendCategoryAliasRequest(chatId,
                                userState.updateId())
                        ,
                        () -> handleMissingUserState(chatId)
                );
    }

    private void sendCategoryAliasRequest(long chatId, Long updateId) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Укажите псевдоним категории:")
                    .build();

            telegramClient.execute(message);
            userStateService.setUserState(chatId,
                    UserState.WAITING_CATEGORY_ALIAS,
                    updateId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMissingUserState(long chatId) {
        try {
            SendMessage errorMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Ошибка: Состояние пользователя не найдено")
                    .build();

            telegramClient.execute(errorMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
