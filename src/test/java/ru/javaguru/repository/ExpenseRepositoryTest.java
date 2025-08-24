package ru.javaguru.repository;

import ru.javaguru.AppRunner;
import ru.javaguru.db.entity.Expense;
import ru.javaguru.db.repository.ExpenseRepository;
import ru.javaguru.dto.ExpenseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ContextConfiguration(classes = AppRunner.class)
@SpringBootTest
@Transactional
public class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void findExpenseByCurrentDayOrderByDesc() {
        List<Object[]> expenses = expenseRepository.findExpenseByCurrentDayOrderByDesc();
        assertThat(expenses).hasSizeGreaterThan(0);
        expenses.forEach(System.out::println);
    }

    @Test
    public void findExpenseByCurrentWeekOrderByDesc() {
        List<Object[]> expenses = expenseRepository.findExpenseByCurrentWeekOrderByDesc();
        assertThat(expenses).hasSizeGreaterThan(0);
        expenses.forEach(System.out::println);
    }

    @Test
    public void findExpenseByCurrentMonthOrderByDesc() {
        List<Object[]> expenses = expenseRepository.findExpenseByCurrentMonthOrderByDesc();
        assertThat(expenses).hasSizeGreaterThan(0);
        expenses.forEach(System.out::println);
    }

}
