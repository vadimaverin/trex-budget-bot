package ru.javaguru.states;

import ru.javaguru.dto.CommonInfo;
import org.springframework.scheduling.annotation.Async;

public interface State {

    UserState state();
    @Async
    void handleState(CommonInfo commonInfo);

}
