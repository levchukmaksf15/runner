package com.karambol.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SupportBot extends TelegramLongPollingBot {

    private final long adminChatId = 376457703L;
    private final Map<Long, Long> adminReplyContext = new HashMap<>();
    private Integer messageId;

    @Override
    public String getBotUsername() {
        return "karambol_pool_bot";
    }

    @Override
    public String getBotToken() {
        return "8033934101:AAH8a7JHsVAWuYnfhvdXjFyUcFt1QnkDOPs";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            messageId = update.getCallbackQuery().getMessage().getMessageId();

            String data = update.getCallbackQuery().getData();
            Long adminId = update.getCallbackQuery().getFrom().getId();

            if (data.startsWith("reply_to:")) {
                Long targetUserId = Long.valueOf(data.split(":")[1]);
                String username = String.valueOf(data.split(":")[2]);
                adminReplyContext.put(adminId, targetUserId);

                send(adminId, "✍️ Ваша відповідь користувачу " + username + ":");
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            String text = update.getMessage().getText();

            if (chatId != adminChatId) {
                if (!text.equals("/start")) {
                    send(chatId, "Дякуємо, ваше повідомлення отримано. Вам нададуть відповідь незабаром, очікуйте, будь ласка.");
                    sendToAdmin(chatId, username, text);
                }
            } else if (adminReplyContext.containsKey(chatId)) {
                EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
                editMarkup.setChatId(chatId.toString());
                editMarkup.setMessageId(messageId);
                editMarkup.setReplyMarkup(null);

                try {
                    execute(editMarkup);
                } catch (Exception ignored) {
                }

                send(adminReplyContext.get(chatId), text);
                send(adminChatId, "✅ Відповідь відправлена користувачу");

                adminReplyContext.remove(chatId);
            }
        }
    }

    private void sendToAdmin(Long userId, String username, String userMessage) {
        InlineKeyboardButton replyButton = new InlineKeyboardButton();
        replyButton.setText("✍️ Відповісти");
        replyButton.setCallbackData("reply_to:" + userId + ":" + username);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(replyButton)));

        SendMessage message = SendMessage.builder()
                .chatId(adminChatId)
                .text("📩 Повідомлення від користувача " + username + ":\n\n" + userMessage)
                .replyMarkup(markup)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException ignored) {
        }
    }

    private void send(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (Exception ignored) {
        }
    }
}
