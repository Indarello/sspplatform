package com.ssp.platform.utl;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.property.EmailAnnouncementProperty;
import com.ssp.platform.property.EmailConnectionProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Component
@EnableAsync
public class EmailSender
{
    private final MimeMessage purchaseCreateMessage;
    private final JavaMailSender emailSender;

    Queue<EmailParams> EmailSendQueue = new PriorityQueue<>(1000, EmailSendComparator);
    private volatile boolean busyQueue = false;
    private volatile boolean busySending = false;
    private volatile boolean busyPurchaseCreate = false;
    int SleepTime = 100;

    private final String host;
    private final int purchaseCreate;
    private final String purchaseCFirstLine;
    private final int purchaseCDescription;
    private final int purchaseCBudget;


    @Autowired
    EmailSender(JavaMailSender emailSender, EmailConnectionProperty emailConnectionProperty, EmailAnnouncementProperty emailAnnouncementProperty) throws MessagingException
    {
        this.emailSender = emailSender;

        purchaseCreateMessage = emailSender.createMimeMessage();
        purchaseCreateMessage.setFrom(new InternetAddress(emailConnectionProperty.getUsername(), false));

        this.purchaseCreate = emailAnnouncementProperty.getPurchaseCreate();
        this.host = emailAnnouncementProperty.getHost();
        this.purchaseCFirstLine = emailAnnouncementProperty.getPurchaseCFirstLine();
        this.purchaseCDescription = emailAnnouncementProperty.getPurchaseCDescription();
        this.purchaseCBudget = emailAnnouncementProperty.getPurchaseCBudget();

        purchaseCreateMessage.setSubject(emailAnnouncementProperty.getPurchaseCSubject(), "UTF-8");

    }

    @Async
    public void sendMailPurchaseCreate(Purchase purchase, List<User> users) throws MessagingException, InterruptedException
    {
        if (purchaseCreate == 0 || users.size() == 0) return;

        while(busyPurchaseCreate)
        {
            Thread.sleep(SleepTime);
        }
        busyPurchaseCreate = true;

        String content = purchaseCFirstLine + "<br>";
        content = content + "Название закупки: " + purchase.getName() + "<br>";

        if (purchaseCDescription == 1)
        {
            content = content + "Описание закупки: " + purchase.getDescription() + "<br>";
        }
        if (purchaseCBudget == 1)
        {
            Long budget = purchase.getBudget();
            if (budget > 0) content = content + "Бюджет закупки: " + budget + "<br>";
        }

        content = content + "Закупка доступна по адресу: " + host + "/purchase/" + purchase.getId();
        purchaseCreateMessage.setContent(content, "text/html; charset=UTF-8");

        Date nowDate = new Date();
        purchaseCreateMessage.setSentDate(nowDate);

        List<EmailParams> emailParamsList = new ArrayList<>(users.size());

        for (User user : users)
        {
            emailParamsList.add(new EmailParams(3, purchaseCreateMessage, user.getEmail(), nowDate));
        }

        busyPurchaseCreate = false;

        addToQueue(emailParamsList);
    }

    @Async
    public void addToQueue(EmailParams emailParams) throws InterruptedException, MessagingException
    {
        while(busyQueue)
        {
            Thread.sleep(SleepTime);
        }
        busyQueue = true;
        EmailSendQueue.add(emailParams);
        busyQueue = false;
        if(!busySending) beginSendMail();
    }

    @Async
    public void addToQueue(List<EmailParams> emailParamsList) throws InterruptedException, MessagingException
    {
        while(busyQueue)
        {
            Thread.sleep(SleepTime);
        }

        busyQueue = true;
        EmailSendQueue.addAll(emailParamsList);
        busyQueue = false;

        if(!busySending)
        {
            beginSendMail();
        }
    }

    @Async
    public void beginSendMail() throws MessagingException, InterruptedException
    {
        busySending = true;

        while(busyQueue)
        {
            Thread.sleep(SleepTime);
        }

        busyQueue = true;
        EmailParams params = EmailSendQueue.poll();

        if(params == null)
        {
            busySending = false;
            busyQueue = false;
            return;
        }
        busyQueue = false;

        MimeMessage message = params.getMessage();

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params.getEmail()));
        emailSender.send(message);

        beginSendMail();
    }

    public static Comparator<EmailParams> EmailSendComparator = new Comparator<EmailParams>()
    {
        @Override
        public int compare(EmailParams c1, EmailParams c2)
        {
            int priority1 = c1.getPriority();
            int priority2 = c2.getPriority();

            if (priority1 != priority2) return priority1 - priority2;
            else return c1.getDate().compareTo(c2.getDate());
        }
    };

}