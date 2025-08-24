package ru.javaguru.service;


//import ru.javaguru.db.repository.UserRepository;
import ru.javaguru.db.repository.UserRepository;
import ru.javaguru.dto.UserStateDto;
import ru.javaguru.states.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserRepository userRepository;

    public Optional<UserStateDto> getUserStateDto(Long chatId) {
        return userRepository.getUserStateDtoByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public UserState getUserState(long chatId) {
        return userRepository.getUserStateByChatId(chatId);
    }

    @Transactional
    public void setUserState(long chatId, UserState userState, long updateId) {
        userRepository.setUserStateByChatId(chatId, userState, updateId);
        log.info("Set user state chatId: {} userState: {} updateId: {}",
                chatId,
                userState,
                updateId);
    }

    @Transactional
    public void setUserState(long chatId, UserState userState) {
        userRepository.setUserStateByChatId(chatId, userState);
        log.info("Set user state chatId: {} userState: {}",
                chatId,
                userState);
    }

    @Transactional
    public void clearUserState(long chatId) {
        userRepository.setUserStateByChatId(chatId, UserState.NONE, null);
        log.info("Clear user state chatId: {}, userState: {} updateId: {}",
                chatId,
                UserState.NONE,
                null);
    }
}

