package ru.javaguru.db.repository;

import ru.javaguru.db.entity.User;
import ru.javaguru.dto.UserStateDto;
import ru.javaguru.states.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByChatIdAndUserName(long chatId, String userName);

    @Transactional
    @Query("SELECT u.state, u.updateId FROM User u WHERE u.chatId = :chatId")
    Optional<UserStateDto> getUserStateDtoByChatId(@Param("chatId") long chatId);

    @Transactional
    @Query("SELECT u.state FROM User u WHERE u.chatId = :chatId")
    UserState getUserStateByChatId(@Param("chatId") long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.state = :userState WHERE u.chatId = :chatId")
    void setUserStateByChatId(@Param("chatId") long chatId,
                              @Param("userState") UserState userState);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.state = :userState, u.updateId = :updateId WHERE u.chatId = :chatId")
    void setUserStateByChatId(@Param("chatId") long chatId,
                              @Param("userState") UserState userState,
                              @Param("updateId") Long updateId);


}
