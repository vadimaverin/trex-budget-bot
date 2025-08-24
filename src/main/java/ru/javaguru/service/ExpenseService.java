package ru.javaguru.service;

import ru.javaguru.db.entity.Expense;
import ru.javaguru.db.repository.ExpenseRepository;
import ru.javaguru.dto.ExpenseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public List<ExpenseDto> findDay() {
        return expenseRepository.findExpenseByCurrentDayOrderByDesc()
                .stream()
                .map(row -> new ExpenseDto((String) row[0], ((Number) row[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> findWeek() {
        return expenseRepository.findExpenseByCurrentWeekOrderByDesc()
                .stream()
                .map(row -> new ExpenseDto((String) row[0], ((Number) row[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> findMonth() {
        return expenseRepository.findExpenseByCurrentMonthOrderByDesc()
                .stream()
                .map(row -> new ExpenseDto((String) row[0], ((Number) row[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<Expense> create(Expense expense) {
        Expense newExpense = expenseRepository.save(expense);
        return Optional.of(newExpense);
    }

}
