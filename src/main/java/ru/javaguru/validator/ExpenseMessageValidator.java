package ru.javaguru.validator;

import ru.javaguru.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseMessageValidator {

    private final CategoryService categoryService;

    public ValidationResult validate(String message) {

        ValidationResult result = new ValidationResult();
        if (message == null) {
            result.addError(Error.of(1, "Message is required"));
            return result;
        }

        String[] parts = message.split(" ");
        if (parts.length < 2) {
            String errorMessage = "Некорректный формат. Правильный формат: {Сумма} {Категрия} [Описание].";
            result.addError(Error.of(2, errorMessage));
        }

//        boolean hasErrorCode2 = findErrorByCode(result, 2);
//        if (!hasErrorCode2 && !parts[0].matches("\\d+")) {
//            result.addError(Error.of(3, "Значение суммы не является числом."));
//        }

        boolean hasErrorCode2 = findErrorByCode(result, 2);
        if (!hasErrorCode2) {
            try {
                Double.parseDouble(parts[0].replace(",","."));
            } catch (NumberFormatException e) {
                result.addError(Error.of(3, "Значение суммы не является числом."));
            }
        }

        if (!hasErrorCode2
                && !(categoryService.existsByName(parts[1])
                    || categoryService.existsByAlias(parts[1]))) {
            result.addError(Error.of(4, "Не найдена категория."));
        }

        return result;

    }

    public boolean findErrorByCode(ValidationResult result, int code) {
        return result.getErrors().stream()
                .anyMatch(error -> code == error.getCode());
    }

}
