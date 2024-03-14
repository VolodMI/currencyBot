package com.firstbot.ua.CurrencyRateBot;

import com.firstbot.ua.CurrencyRateBot.config.BotConfig;
import com.firstbot.ua.CurrencyRateBot.model.CurrencyModel;
import com.firstbot.ua.CurrencyRateBot.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        CurrencyModel currencyModel = new CurrencyModel();
        String currency = "";

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (messageText) {
                case "/start":
                    System.out.println("Start");
                    startCommandReceived(chatId, update.getMessage().getChat().getUserName());
                    break;
                default:
                    try {
                        currency = CurrencyService.getCurrencyRate(messageText, currencyModel);
                    } catch (IOException e) {
                        sendMessage(chatId, "Error");
                    }
                    sendMessage(chatId, currency);
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + "! I can show you official currency rate";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Failed");
        }
    }

}
