package com.project.MathBuddyBot.service;

import com.project.MathBuddyBot.MathExpressions;
import com.project.MathBuddyBot.Model;
import com.project.MathBuddyBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    Integer counter = 0;
    Integer maxRate = 0;
    String answerContainer;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }



    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        String messageText = update.getMessage().getText();
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/solve":
                    try {
                        MathExpressions.getInfo(model);
                        answerContainer = String.valueOf(model.getAnswer());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                        sendMessage(chatId, "Найди значение: " + model.getExpression());
                    break;
                case "/rate":
                    sendMessage(chatId, "Ваш максимальный счёт: " + maxRate);
                    break;
                default:
                    if (messageText.equals(answerContainer)) {
                        counter += 1;
                        sendMessage(chatId, "Верно");
                    } else {
                        sendMessage(chatId, "Неверно! Попробуй ещё раз");
                        if (maxRate < counter){
                            maxRate = counter;
                            counter = 0;
                        }
                    }

            }

        }
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("/solve");
        row.add("/rate");

        keyboardRowList.add(row);

        keyboardMarkup.setKeyboard(keyboardRowList);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        }
            catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
    }

}
