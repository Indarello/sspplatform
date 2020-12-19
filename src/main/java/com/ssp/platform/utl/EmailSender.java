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
    private final int purchaseCreateStatus;
    private final String purchaseCreateFirstLine;
    private final int purchaseCreateDescription;
    private final int purchaseCreateBudget;


    @Autowired
    EmailSender(JavaMailSender emailSender, EmailConnectionProperty emailConnectionProperty, EmailAnnouncementProperty emailAnnouncementProperty) throws MessagingException
    {
        this.emailSender = emailSender;

        purchaseCreateMessage = emailSender.createMimeMessage();
        purchaseCreateMessage.setFrom(new InternetAddress(emailConnectionProperty.getUsername(), false));

        this.purchaseCreateStatus = emailAnnouncementProperty.getStatus();
        this.host = emailAnnouncementProperty.getHost();
        this.purchaseCreateFirstLine = emailAnnouncementProperty.getFirstLine();
        this.purchaseCreateDescription = emailAnnouncementProperty.getDescription();
        this.purchaseCreateBudget = emailAnnouncementProperty.getBudget();

        purchaseCreateMessage.setSubject(emailAnnouncementProperty.getSubject(), "UTF-8");

    }

    @Async
    public void sendMailPurchaseCreate(Purchase purchase, List<User> users) throws MessagingException, InterruptedException
    {
        if (purchaseCreateStatus == 0 || users.size() == 0) return;

        while(busyPurchaseCreate)
        {
            Thread.sleep(SleepTime);
        }
        busyPurchaseCreate = true;

        String content = purchaseCreateFirstLine + "<br>";
        content = content + "Название закупки: " + purchase.getName() + "<br>";

        if (purchaseCreateDescription == 1)
        {
            content = content + "Описание закупки: " + purchase.getDescription() + "<br>";
        }
        if (purchaseCreateBudget == 1)
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
        System.out.println("-Приступаем к добавлению в очередь отправки записи");
        while(busyQueue)
        {
            System.out.println("-Queue занята, ждем");
            Thread.sleep(SleepTime);
        }
        busyQueue = true;
        System.out.println("-Очередь осовободилась, добавляем записи");
        EmailSendQueue.addAll(emailParamsList);
        busyQueue = false;
        System.out.println("-Закончили добавлять записи");
        if(!busySending)
        {
            System.out.println("-Отправка Email была приостановлена, запускаем вновь");
            beginSendMail();
        }
    }

    @Async
    public void beginSendMail() throws MessagingException, InterruptedException
    {
        busySending = true;
        System.out.println("+Приступаем к отправке письма");

        while(busyQueue)
        {
            System.out.println("+Queue занята, ждем");
            Thread.sleep(SleepTime);
        }

        busyQueue = true;
        System.out.println("+Очередь осовободилась, берем записи");
        EmailParams params = EmailSendQueue.poll();

        if(params == null)
        {
            System.out.println("+В очереди больше нет записей, останавливаем отправку на email");
            busySending = false;
            busyQueue = false;
            return;
        }
        busyQueue = false;

        MimeMessage message = params.getMessage();
        System.out.println("+взяли запись из очереди и отправляем email");

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params.getEmail()));
        emailSender.send(message);

        System.out.println("+Отправили email, запуск повторной");

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