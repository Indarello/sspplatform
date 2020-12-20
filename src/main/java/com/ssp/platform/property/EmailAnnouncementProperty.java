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
    private int sendCoolDown;
    private String host;
    private String purchaseCSubject;
    private String purchaseCFirstLine;
    private int purchaseCDescription;
    private int purchaseCBudget;
    private String supplyESubject;
    private String supplyEFirstLine;
    private String answerSubject;
    private String answerCFirstLine;
    private String answerEFirstLine;

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
    public String getSupplyESubject()
    {
        return new String(supplyESubject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    public String getSupplyEFirstLine()
    {
        return new String(supplyEFirstLine.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    public String getAnswerSubject()
    {
        return new String(answerSubject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    public String getAnswerCFirstLine()
    {
        return new String(answerCFirstLine.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    public String getAnswerEFirstLine()
    {
        return new String(answerEFirstLine.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
