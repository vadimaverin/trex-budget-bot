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

import static ru.javaguru.utils.InlineKeyboardBuilder.getInlineKeyboardMarkupDetailedExpenses;

@Component
@RequiredArgsConstructor
public class ListCurrentExpense implements Command {

    private final TelegramClient telegramClient;
    private final ExpenseService expenseService;

    @Override
    public BotCommand command() {
        return BotCommand.LISTCURRENTEXPENSE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        double totalDay = sum(expenseService.findDay());
        double totalWeek = sum(expenseService.findWeek());
        double totalMonth = sum(expenseService.findMonth());

        String message = String.format("""
                                📊 Твои текущие расходы
                                — — — — — — — — — —
                                День: %.2f ₽
                                Неделя: %.2f ₽
                                Месяц: %.2f ₽""",
                totalDay, totalWeek, totalMonth);

        long chatId = commonInfo.getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.valueOf(message))
                .replyMarkup(getInlineKeyboardMarkupDetailedExpenses())
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
