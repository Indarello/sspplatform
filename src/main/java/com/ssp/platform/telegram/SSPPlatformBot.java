package com.ssp.platform.telegram;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.User;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.impl.UserServiceImpl;
import com.ssp.platform.telegram.Service.TelegramUsersService;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RestController
public class SSPPlatformBot extends TelegramLongPollingBot {
    private final String username = "SSPPlatformBot";
    private final String token = "1287383695:AAHvUq-PkENQgGdyBKRhzGj39_UYgia3OWo";

    private final String COMMAND_START = "/start";
    private final String COMMAND_STOP = "/stop";

    private final String MASK_WELCOME_MESSAGE =
            "Здравствуйте!\n" +
            "Для получения оповещений введите в вашем личном кабинете этот код: *%s*";

    private final String MASK_WELCOME_ERROR_MESSAGE =
            "Ваш Telegram аккаунт и SSP аккаунт уже связаны :(";

    private final String MASK_SUCCEED_CONNECT_MESSAGE =
            "Аккаунт успешно привязан! Здравствуйте, %s";

    private final String MASK_GOODBYE_MESSAGE =
            "До свидания, %s...";

    private final String MASK_UNDEFINED_COMMAND =
            "К сожалению, я не андерстенд, что вы только что сказали";

    private final String MASK_ANSWER_NOTIFY =
            "Здравствуйте, %s!\n" +
            "На Ваш вопрос:\n\n" +
            "*%s*\n" +
            "_%s_\n\n" +
            "был добавлен ответ:\n" +
            "_%s_";

    private final TelegramUsersService telegramUsersService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserServiceImpl userService;

    private final TelegramPurchaseNotify notify;

    private final Logger logger = LoggerFactory.getLogger(SSPPlatformBot.class);

    public SSPPlatformBot(
            TelegramUsersService telegramUsersService, UserDetailsServiceImpl userDetailsService, UserServiceImpl userService,
            TelegramPurchaseNotify notify
    ) {
        this.telegramUsersService = telegramUsersService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.notify = notify;
    }

    @PostMapping("/bot")
    public ResponseEntity<Object> connectUserAccount(@RequestHeader("Authorization") String token, @RequestParam("code") int code) throws TelegramException {
        User user = userDetailsService.loadUserByToken(token);
        telegramUsersService.connectUserByCode(user, code);

        SendMessage connectSucceed = new SendMessage();
        connectSucceed.setChatId(String.valueOf(telegramUsersService.getChatIdByUser(user)));
        connectSucceed.setText(String.format(MASK_SUCCEED_CONNECT_MESSAGE, user.getFirstName()));

        try {
            execute(connectSucceed);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ApiResponse(true, "Аккаунты связаны"), HttpStatus.OK);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        logger.info(message.getText());

        switch (message.getText()){
            case COMMAND_START:
                start(message.getChatId());
                break;

            case COMMAND_STOP:
                stop(message.getChatId());
                break;

            default:
                undefined(message.getChatId());
        }
    }

    public void notifyAboutAnswer(Question question){
        if (!telegramUsersService.existsByUsername(question.getAuthor().getUsername())) return;

        SendMessage answerNotify = new SendMessage();
        answerNotify.setText(String.format(
                MASK_ANSWER_NOTIFY,
                question.getAuthor().getFirstName(),
                question.getName(),
                question.getDescription(),
                question.getAnswer().getDescription()));

        answerNotify.setChatId(String.valueOf(telegramUsersService.getChatIdByUser(question.getAuthor())));
        answerNotify.setParseMode("Markdown");

        try {
            execute(answerNotify);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void notifyAllAboutPurchase(Purchase purchase){

        SendMessage purchaseNotify = new SendMessage();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

        keyboardButton.setText("Подать предложение");
        keyboardButton.setUrl("https://ssp-platform.herokuapp.com/purchase/0/" + purchase.getId());

        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
        keyboardButtonRow.add(keyboardButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonRow);

        keyboardMarkup.setKeyboard(rowList);
        purchaseNotify.setReplyMarkup(keyboardMarkup);
        purchaseNotify.setParseMode("Markdown");

        List<TelegramUsersEntity> users = telegramUsersService.getAllConnectedUsers();

        for (TelegramUsersEntity user : users){
            notify.notifyOne(this, purchaseNotify, purchase, user);
        }
    }

    private void start(Long chatId){
        if (telegramUsersService.existsByChatId(chatId)){
            SendMessage welcomeErrorMessage = new SendMessage();
            welcomeErrorMessage.setText(MASK_WELCOME_ERROR_MESSAGE);
            welcomeErrorMessage.setChatId(String.valueOf(chatId));

            try{
                execute(welcomeErrorMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage welcomeMessage = new SendMessage();

            Random random = new Random(System.currentTimeMillis());
            final int max = 999999;
            final int min = 100000;
            int securityCode = random.nextInt(max - min) + min;

            welcomeMessage.setText(String.format(MASK_WELCOME_MESSAGE, securityCode));
            welcomeMessage.setChatId(String.valueOf(chatId));
            welcomeMessage.setParseMode("Markdown");

            telegramUsersService.createChat(chatId, securityCode);

            try{
                execute(welcomeMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void stop(Long chatId){
        Optional<User> user = userService.findByUsername(telegramUsersService.getUsernameByChatId(chatId));

        SendMessage goodbyeMessage = new SendMessage();
        goodbyeMessage.setText(String.format(MASK_GOODBYE_MESSAGE, user.get().getFirstName()));
        goodbyeMessage.setChatId(String.valueOf(chatId));

        try{
            execute(goodbyeMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        telegramUsersService.deleteChat(chatId);
    }

    private void undefined(Long chatId){
        SendMessage undefinedMessage = new SendMessage();
        undefinedMessage.setText(MASK_UNDEFINED_COMMAND);
        undefinedMessage.setChatId(String.valueOf(chatId));

        try{
            execute(undefinedMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
