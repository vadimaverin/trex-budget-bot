package ru.javaguru.routers;

import ru.javaguru.command.Command;
import ru.javaguru.command.BotCommand;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandRouter {

    private final Map<BotCommand, Command> handlerMap = new EnumMap<>(BotCommand.class);

    public CommandRouter(List<Command> handlers) {
        handlers.forEach(h -> handlerMap.put(h.command(), h));
    }

    public Optional<Command> getHandler(BotCommand command) {
        return Optional.ofNullable(handlerMap.get(command));
    }

}
