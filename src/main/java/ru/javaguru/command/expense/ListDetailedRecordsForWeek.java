package ru.javaguru.command.expense;

import ru.javaguru.command.BotCommand;
import ru.javaguru.command.Command;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.dto.ExpenseDto;
import ru.javaguru.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.stream.Collectors;

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardGoToStartCommand;

@Component
@RequiredArgsConstructor
public class ListDetailedRecordsForWeek implements Command {

    private final TelegramClient telegramClient;
    private final ExpenseService expenseService;

    @Override
    public BotCommand command() {
        return BotCommand.DETALIEDRECORDSFORWEEK;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        List<ExpenseDto> expensesForDay = expenseService.findWeek();

        String message = expensesForDay.stream()
                .map(expense -> String.format(
                        "%s | %.1f",
                        expense.categoryName(),
                        expense.amount()
                ))
                .collect(Collectors.joining("\n"));

        double totalAmount = sum(expensesForDay);
        String fullMessage = String.format("""
                         📊 Твои расходы за неделю
                         
                         💰 Итого: %.1f
                         — — — — — — — — — —
                         %s ₽""",
                totalAmount,
                message);

        long chatId = commonInfo.getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(fullMessage)
                .replyMarkup(getInlineKeyboardGoToStartCommand())
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    double sum(List<ExpenseDto> expenses) {
        return  expenses.stream()
                .mapToDouble(ExpenseDto::amount)
                .sum();
    }

}
