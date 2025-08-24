package ru.javaguru.dto;

import ru.javaguru.states.UserState;

public record UserStateDto(UserState userState, Long updateId) {
}
