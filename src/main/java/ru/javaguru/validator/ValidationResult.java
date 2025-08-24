package ru.javaguru.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationResult {

    private final List<Error> errors = new ArrayList<>();

    public void addError(Error error) {
        errors.add(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

}