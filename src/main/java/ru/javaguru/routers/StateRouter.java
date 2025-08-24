package ru.javaguru.routers;

import ru.javaguru.states.State;
import ru.javaguru.states.UserState;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StateRouter {

    private final Map<UserState, State> handlerMap = new EnumMap<>(UserState.class);

    public StateRouter(List<State> handlers) {
        handlers.forEach(h -> handlerMap.put(h.state(), h));
    }

    public Optional<State> getHandler(UserState state) {
        return Optional.ofNullable(handlerMap.get(state));
    }

}
