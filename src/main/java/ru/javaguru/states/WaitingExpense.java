package ru.javaguru.states;

import ru.javaguru.db.entity.Category;
import ru.javaguru.db.entity.Expense;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.dto.ExpenseDto;
import ru.javaguru.service.CategoryService;
import ru.javaguru.service.ExpenseService;
import ru.javaguru.service.UserStateService;
import ru.javaguru.validator.Error;
import ru.javaguru.validator.ExpenseMessageValidator;
import ru.javaguru.validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardMarkupAddExpense;

@Component
@RequiredArgsConstructor
public class WaitingExpense implements State {

    private final TelegramClient telegramClient;
    private final ExpenseMessageValidator expenseMessageValidator;

    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final UserStateService userStateService;


    @Override
    public UserState state() {
        return UserState.WAITING_EXPENSE;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();

        ValidationResult validateResult = expenseMessageValidator.validate(messageText);
        if (validateResult.isValid()) {

            String[] parts = messageText.split(" ");
            double amount = Double.parseDouble(parts[0]
                    .replace(",", ".")
                    .replace("-", ""));

            String catName = parts[1];
            Optional<Category> category = categoryService.findByNameOrAlias(catName);

            String description;
            if (parts.length > 2) {
                description = getDescriptionString(parts);
            } else {
                description = "-";
            }
            
            Expense expense = Expense.builder()
                    .date(LocalDateTime.now())
                    .amount(amount)
                    .category(category.get())
                    .description(description)
                    .build();

            Optional<Expense> newExpense = expenseService.create(expense);

            if (newExpense.isPresent()) {
                Expense exp  = newExpense.get();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = exp.getDate().format(formatter);

                double totalDay = sum(expenseService.findDay());
                double totalWeek = sum(expenseService.findWeek());
                double totalMonth = sum(expenseService.findMonth());

                String message1 = String.format("""
                                ✅ Расход добавлен!
                                
                                📅 %s | %s | %.2f ₽
                                
                                📊 Твои текущие расходы
                                День: %.2f ₽
                                Неделя: %.2f ₽
                                Месяц: %.2f ₽
                                """,
                        formattedDate,
                        exp.getCategory().getName(),
                        exp.getAmount(),
                        totalDay, totalWeek, totalMonth);
                sendMessage(chatId, message1);

            }
            userStateService.clearUserState(chatId);

        } else {

            String errorsMessage = validateResult.getErrors()
                    .stream()
                    .map(Error::getMessage)
                    .collect(Collectors.joining(" "));
            sendMessage(chatId, errorsMessage);
        }
    }

    String getDescriptionString(String[] parts) {        
        return Arrays.stream(parts)
            .skip(2)
            .collect(Collectors.joining(" "));
    }

    void sendMessage(Long chatId, String message) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(getInlineKeyboardMarkupAddExpense())
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    int sum(List<ExpenseDto> list) {
        return  list.stream()
                .mapToInt(s -> (int) s.amount()).sum();
    }

}
