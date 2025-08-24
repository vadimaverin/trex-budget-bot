package ru.javaguru.repository;

import ru.javaguru.AppRunner;
import ru.javaguru.db.entity.User;
import ru.javaguru.db.repository.UserRepository;
import ru.javaguru.dto.UserStateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = AppRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserByChatIdAndUserName() {
        Optional<User> user = userRepository.getUserByChatIdAndUserName(213773099L, "kddvad1m");
        assertTrue(user.isPresent());
    }

    @Test
    void getUserStateDto() {
        Optional<UserStateDto> userStateDto = userRepository.getUserStateDtoByChatId(213773099L);
        assertTrue(userStateDto.isPresent());
    }

}
