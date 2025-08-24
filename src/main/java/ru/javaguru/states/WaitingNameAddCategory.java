package ru.javaguru.states;

import ru.javaguru.db.entity.Category;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.service.CategoryService;
import ru.javaguru.service.UserStateService;
import ru.javaguru.utils.InlineKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardMarkupFillAlias;

@Component
@RequiredArgsConstructor
public class WaitingNameAddCategory implements State{

    private final CategoryService categoryService;
    private final UserStateService userStateService;
    private final TelegramClient telegramClient;

    public UserState state() {
        return UserState.WAITING_NAME_ADD_CATEGORY;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        String newNameCategory = commonInfo.getMessageText();

        if (categoryService.existsByName(newNameCategory)) {

            String template = "Категория с именем %s уже существет.";
            String message = String.format(template, newNameCategory);
            sendMessage(chatId, message, 0L);

        } else {

            Category newCategory = Category.builder().name(newNameCategory).build();
            Category newCat = categoryService.create(newCategory);

            long updateId = newCategory.getId();

            String template = "Категория добавлена! ID: %d, %s";
            String message = String.format(template, newCategory.getId(), newCat.getName());
             sendMessage(chatId, message, updateId);
        }
    }

    void sendMessage(Long chatId, String message, long updateId) {

        SendMessage sendMessageSuccess = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        SendMessage sendMessageFillAlias = SendMessage.builder()
                .chatId(chatId)
                .text("Хотите заполнить псевдонимы для категории?")
                .replyMarkup(getInlineKeyboardMarkupFillAlias())
                .build();
        try {

            telegramClient.execute(sendMessageSuccess);
            telegramClient.execute(sendMessageFillAlias);
            userStateService.setUserState(chatId,
                    UserState.WAITING_NAME_ADD_CATEGORY,
                    updateId);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}
