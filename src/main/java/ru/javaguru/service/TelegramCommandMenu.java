package ru.javaguru.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramCommandMenu {

    private final TelegramClient sender;

    @PostConstruct
    public void registerCommands() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand("start", "Запуск бота"),
                    new BotCommand("help", "Помощь")
            );
            sender.execute(new SetMyCommands(commands, BotCommandScopeDefault.builder().build(), null));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register commands: " + e.getMessage(), e);
        }
    }

}
