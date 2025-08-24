package ru.javaguru.command;

import ru.javaguru.dto.CommonInfo;
import org.springframework.scheduling.annotation.Async;

public interface Command {

    BotCommand command();

    @Async
    void execute(CommonInfo commonInfo);

}
