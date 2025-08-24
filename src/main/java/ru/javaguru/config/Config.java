package ru.javaguru.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class Config {

    @Primary
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("vthread-", 0).factory());
    }

    @Bean
    public String botToken(@Value("${bot.botToken}") String botToken) {
        return botToken;
    }

    @Bean
    public TelegramClient getTelegramClient(@Autowired String botToken){
        return new OkHttpTelegramClient(botToken);
    }

}
