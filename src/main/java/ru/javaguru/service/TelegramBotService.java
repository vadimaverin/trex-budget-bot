package ru.javaguru.service;

import ru.javaguru.command.BotCommand;
import ru.javaguru.dto.CommonInfo;
import ru.javaguru.routers.CommandRouter;
import ru.javaguru.routers.StateRouter;
import ru.javaguru.states.UserState;
import ru.javaguru.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String botToken;
    private final CommandRouter commandRouter;
    private final StateRouter stateRouter;
    private final UserStateService userStateService;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    @SneakyThrows
    public void consume(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String lastMessage = message.getText();

            CommonInfo commonInfo = getCommonInfo(message);

            if (lastMessage.startsWith("/")) {

                if (commonInfo.isUpdate()) {
                    commandHandler(commonInfo.getMessageText(), commonInfo);
                } else {
                    commandHandler(lastMessage, commonInfo);
                }
                return;
            }

            Long chatId = message.getChatId();
            UserState state = userStateService.getUserState(chatId);
            stateRouter.getHandler(state)
                    .ifPresentOrElse(
                            h -> h.handleState(commonInfo),
                            () -> unknownActionHandler(chatId));
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            CommonInfo commonInfo = getCommonInfo(callbackQuery);
            commandHandler(data, commonInfo);
        }
    }

    private void commandHandler(String userCommand, CommonInfo commonInfo) {

        BotCommand command = BotCommand.fromString(userCommand);
        commandRouter.getHandler(command)
                .ifPresentOrElse(handler -> {
                            handler.execute(commonInfo);
                        },
                        () -> unknownActionHandler(commonInfo.getChatId()));

    }

    private void unknownActionHandler(long chatId) {

        String messageText = """
                Неизвестная команда. Воспользуйтесь командой:

                /start - начало работы и регистрация пользователя
                /help  - вывести список возможных команд
                """;

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private CommonInfo getCommonInfo(Message message) {

        String[] msgParts;
        if (message.getText().startsWith("/")) {
            msgParts = Utils.parseUpdateMassage(message.getText());
            if ((msgParts.length == 2)) {

                Long updateId = Long.parseLong(msgParts[1]);
                return CommonInfo.builder()
                        .chatId(message.getChatId())
                        .userFromTelegram(message.getFrom())
                        .isUpdate(true)
                        .updateId(updateId)
                        .messageText(msgParts[0])
                        .build();
            }
        }

        return CommonInfo.builder()
                .chatId(message.getChatId())
                .userFromTelegram(message.getFrom())
                .messageText(message.getText())
                .build();
    }

    private CommonInfo getCommonInfo (CallbackQuery callbackQuery){
        return CommonInfo.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .userFromTelegram(callbackQuery.getFrom())
                .build();
    }
}
