package com.ssp.platform.telegram;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public final class SSPPlatformBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME = "SSPPlatformBot";
    private final String BOT_TOKEN = "1287383695:AAHvUq-PkENQgGdyBKRhzGj39_UYgia3OWo";

    private final PurchaseService purchaseService;

    public SSPPlatformBot(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage answer = new SendMessage();
        answer.setText("Список закупок:\n");
        answer.setChatId(message.getChatId().toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if (purchaseService == null){
            System.out.println("NULL!");
            answer = new SendMessage();
            answer.setText("Пусто :(");
            answer.setChatId(message.getChatId().toString());
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        answer = new SendMessage();
        answer.setChatId(message.getChatId().toString());
        Pageable pageable = PageRequest.of(0, 10);

        List<Purchase> purchases = purchaseService.getAll(pageable).toList();

        String text = "";
        for (Purchase purchase : purchases){
            text += purchase.getName() + "\n";
        }

        answer.setText(text);

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
