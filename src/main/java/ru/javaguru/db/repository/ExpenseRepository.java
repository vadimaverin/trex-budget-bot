package ru.javaguru.db.repository;

import ru.javaguru.db.entity.Expense;
import ru.javaguru.dto.ExpenseDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

   @Query("""
        SELECT c.name, SUM(e.amount + 0.0)
        FROM Expense e
        JOIN e.category c
        WHERE FUNCTION('DATE', e.date) = CURRENT_DATE
        GROUP BY c.name
        ORDER BY SUM(e.amount) DESC""")
   List<Object[]> findExpenseByCurrentDayOrderByDesc();

   @Query(value = """
        SELECT c.name, SUM(e.amount + 0.0)
        FROM expenses e
        JOIN categories c ON c.id = e.category_id
        WHERE EXTRACT(WEEK FROM e.date) = EXTRACT(WEEK FROM CURRENT_DATE)
        AND EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY c.name
        ORDER BY SUM(e.amount) DESC
        """, nativeQuery = true)
   List<Object[]> findExpenseByCurrentWeekOrderByDesc();

   @Query(value = """
        SELECT c.name, SUM(e.amount + 0.0)
        FROM expenses e
        JOIN categories c ON c.id = e.category_id
        WHERE EXTRACT(MONTH FROM e.date) = EXTRACT(MONTH FROM CURRENT_DATE)
        AND EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY c.name
        ORDER BY SUM(e.amount) DESC
        """, nativeQuery = true)
   List<Object[]> findExpenseByCurrentMonthOrderByDesc();



}
