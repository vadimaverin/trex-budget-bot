package ru.javaguru.validator;

import ru.javaguru.AppRunner;
import ru.javaguru.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = AppRunner.class)
@SpringBootTest
@Transactional
class ExpenseMessageValidatorTest {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExpenseMessageValidator expenseMessageValidator;


    @Test
    void validate_WhenMessageIsNull_ShouldReturnErrorCode1() {
        // Given
        String message = null;

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals(1, result.getErrors().get(0).getCode());
        assertEquals("Message is required", result.getErrors().get(0).getMessage());
    }

    @Test
    void validate_WhenMessageHasOnePartOnly_ShouldReturnErrorCode2() {
        // Given
        String message = "100";

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.getCode() == 2));
        assertEquals("Некорректный формат. Правильный формат: {Сумма} {Категрия} [Описание].", 
                result.getErrors().stream()
                        .filter(error -> error.getCode() == 2)
                        .findFirst()
                        .get()
                        .getMessage());
    }

    @Test
    void validate_WhenAmountIsNotNumber_ShouldReturnErrorCode3() {
        // Given
        String message = "abc тест";
        assertTrue(categoryService.existsByName("тест"));

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.getCode() == 3));
        assertEquals("Значение суммы не является числом.", 
                result.getErrors().stream()
                        .filter(error -> error.getCode() == 3)
                        .findFirst()
                        .get()
                        .getMessage());
    }

    @Test
    void validate_WhenCategoryDoesNotExist_ShouldReturnErrorCode4() {
        // Given
        String message = "100 nonexistent";
        assertFalse(categoryService.existsByName("nonexistent"));

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.getCode() == 4));
        assertEquals("Не найдена категория.", 
                result.getErrors().stream()
                        .filter(error -> error.getCode() == 4)
                        .findFirst()
                        .get()
                        .getMessage());

    }

    @Test
    void validate_WhenValidMessageWithTwoParts_ShouldReturnValidResult() {
        // Given
        String message = "0100,66 Продукты";
        assertTrue(categoryService.existsByName("Продукты"));
        assertTrue(categoryService.existsByAlias("еда"));

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void validate_WhenValidMessageWithThreeParts_ShouldReturnValidResult() {
        // Given
        String message = "150 тест taxi";
        assertTrue(categoryService.existsByName("тест"));

        // When
        ValidationResult result = expenseMessageValidator.validate(message);

        // Then
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());

    }
}