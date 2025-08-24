package ru.javaguru.service;

import ru.javaguru.db.entity.User;
import ru.javaguru.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void findOrCreateUser(long chatId, String username) {
        userRepository.getUserByChatIdAndUserName(chatId, username).orElseGet(() -> {
            User user = new User();
            user.setChatId(chatId);
            user.setUserName(username);
            userRepository.save(user);
            return user;
        });
    }
}
