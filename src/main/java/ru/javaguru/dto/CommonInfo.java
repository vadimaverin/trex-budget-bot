package ru.javaguru.dto;

import lombok.Builder;
import lombok.Value;
import org.telegram.telegrambots.meta.api.objects.User;

@Value
@Builder
public class CommonInfo {
    Long chatId;
    User userFromTelegram;
    String messageText;
    boolean isUpdate;
    Long updateId;
}
