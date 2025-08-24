package ru.javaguru.states;

import ru.javaguru.db.entity.Category;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.dto.UserStateDto;
import ru.javaguru.service.CategoryService;
import ru.javaguru.service.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardMarkupFillAlias;

@Component
@RequiredArgsConstructor
public class WaitingNameRenameCategory implements State {

    private final CategoryService categoryService;
    private final UserStateService userStateService;
    private final TelegramClient telegramClient;

    @Override
    public UserState state() {
        return UserState.WAITING_NAME_UPDATE_CATEGORY;
    }

    @Override
    public void handleState(CommonInfo info) {

        Long chatId = info.getChatId();
        String newCategoryName = info.getMessageText();
        Optional<UserStateDto> userStateDto = userStateService.getUserStateDto(chatId);
        if (userStateDto.isPresent()) {
            UserStateDto stateDto = userStateDto.get();

            Optional<Category> category = categoryService.updateName(stateDto.updateId(), newCategoryName);

            if (category.isPresent()) {

                String template = "Категория обновлена! ID: %d, %s";
                long catId = category.get().getId();
                String textMessage = String.format(template, catId, newCategoryName);
                sendMessage(chatId, textMessage, catId);

            }
        }
    }

    void sendMessage(Long chatId, String message, long updateId) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        SendMessage sendMessageFillAlias = SendMessage.builder()
                .chatId(chatId)
                .text("Хотите заполнить псевдонимы для категории?")
                .replyMarkup(getInlineKeyboardMarkupFillAlias())
                .build();

        try {
            telegramClient.execute(sendMessage);
            telegramClient.execute(sendMessageFillAlias);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
