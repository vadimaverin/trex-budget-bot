package ru.javaguru.command.category;

import ru.javaguru.command.BotCommand;
import ru.javaguru.command.Command;
import ru.javaguru.db.entity.Category;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.service.CategoryService;
import ru.javaguru.service.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardMarkupAddCategories;

@Component
@RequiredArgsConstructor
public class ListCategoryCommand implements Command {

    private final TelegramClient client;
    private final CategoryService categoryService;
    private final UserStateService stateService;

    @Override
    public BotCommand command() {
        return BotCommand.CATEGORIES;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        try {
            List<Category> categories = categoryService.findAll();
            if (categories.isEmpty()) {
                SendMessage message = SendMessage.builder()
                        .chatId(commonInfo.getChatId())
                        .text("У вас пока нет категорий.")
                        .replyMarkup(getInlineKeyboardMarkupAddCategories())
                        .build();

                client.execute(message);
                return;
            }

            StringBuilder cats = new StringBuilder("Ваш список категорий:\n\n");
            for (Category category : categories) {
                cats
                        .append("• ")
                        .append("ID: ")
                        .append(category.getId())
                        .append(" ")
                        .append(category.getName())
                        .append(" ")
                        .append("(").append(category.getAlias()).append(")")
                        .append(" ")
                        .append("/renameCategory")
                        .append(category.getId())
                        .append("\n");
            }

            SendMessage message = SendMessage.builder()
                    .chatId(commonInfo.getChatId())
                    .text(cats.toString())
                    .replyMarkup(getInlineKeyboardMarkupAddCategories())
                    .build();

            client.execute(message);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
