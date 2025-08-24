package ru.javaguru.states;

import ru.javaguru.db.entity.Category;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.dto.UserStateDto;
import ru.javaguru.service.CategoryService;
import ru.javaguru.service.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardGoToStartCommand;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingCategoryAlias implements State{

    private final UserStateService userStateService;
    private final CategoryService categoryService;
    private final TelegramClient telegramClient;

    @Override
    public UserState state() {
        return UserState.WAITING_CATEGORY_ALIAS;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {

        long chatId = commonInfo.getChatId();
        String alias = commonInfo.getMessageText();

        Optional<UserStateDto> userStateDto = userStateService.getUserStateDto(chatId);
        if (userStateDto.isPresent()) {
            UserStateDto userState = userStateDto.get();

            Optional<Category> category = categoryService.updateAlias(userState.updateId(), alias);
            String template = "Псевдоним категории обновлен! ID: %d, %s (%s)";
            category.ifPresent(cat -> {
                String massage = String.format(template,
                        cat.getId(),
                        cat.getName(),
                        cat.getAlias());
                sendMessage(chatId, massage);
            });

        }
    }

    void sendMessage(Long chatId, String message) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(getInlineKeyboardGoToStartCommand())
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
