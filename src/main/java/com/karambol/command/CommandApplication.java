package com.karambol.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new SupportBot());
		} catch (TelegramApiException ignored) {
		}
	}
}
