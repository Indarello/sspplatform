package com.ssp.platform.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Data
@ConfigurationProperties(prefix = "email-announcement")
public class EmailAnnouncementProperty
{
    private int purchaseCreate;
    private int supplyEdit;
    private int answerCreate;
    private int answerEdit;
    private String host;
    private String purchaseCSubject;
    private String purchaseCFirstLine;
    private int purchaseCDescription;
    private int purchaseCBudget;

    public String getHost()
    {
        return new String(host.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getPurchaseCSubject()
    {
        return new String(purchaseCSubject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getPurchaseCFirstLine()
    {
        return new String(purchaseCFirstLine.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

/*
        # ---------------------------  Оповещение при изменении статуса предложения
# Заголовк письма
    email-announcement.supplyESubject=Изменение статуса предложения
# Первая строка в письме
    email-announcement.supplyEFirstLine=Здравствуйте, вашему предложению был присвоен статус
# ---------------------------  Оповещение при ответе на вопрос и изменении ответа
# Заголовк письма
    email-announcement.answerCSubject=Новый ответ на вопрос
# Первая строка в письме
    email-announcement.answerCFirstLine=Здравствуйте, по вашему вопросу был дан ответ:
    */

}
