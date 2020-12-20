package com.ssp.platform.email;

import com.ssp.platform.entity.*;
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
    private final MimeMessage supplyEditMessage;
    private final MimeMessage answerCEMessage;
    private final JavaMailSender emailSender;

    Queue<EmailParams> EmailSendQueue = new PriorityQueue<>(1000, EmailSendComparator);
    private volatile boolean busyQueue = false;
    private volatile boolean busySending = false;
    private volatile boolean busyPurchaseCreate = false;
    private volatile boolean busySupplyEdit = false;
    private volatile boolean busyAnswerCE = false;
    int SleepTime = 100;

    private final int sendCoolDown;
    private final String host;
    private final int purchaseCreate;
    private final int supplyEdit;
    private final int answerCreate;
    private final int answerEdit;
    private final String purchaseCFirstLine;
    private final int purchaseCDescription;
    private final int purchaseCBudget;
    private final String supplyEFirstLine;
    private final String answerCFirstLine;
    private final String answerEFirstLine;

    @Autowired
    EmailSender(JavaMailSender emailSender, EmailConnectionProperty emailConnectionProperty, EmailAnnouncementProperty emailAnnouncementProperty) throws MessagingException
    {
        this.emailSender = emailSender;

        this.purchaseCreate = emailAnnouncementProperty.getPurchaseCreate();
        this.purchaseCFirstLine = emailAnnouncementProperty.getPurchaseCFirstLine();
        this.purchaseCDescription = emailAnnouncementProperty.getPurchaseCDescription();
        this.purchaseCBudget = emailAnnouncementProperty.getPurchaseCBudget();

        this.supplyEdit = emailAnnouncementProperty.getSupplyEdit();
        this.supplyEFirstLine = emailAnnouncementProperty.getSupplyEFirstLine();

        this.answerCreate = emailAnnouncementProperty.getAnswerCreate();
        this.answerEdit = emailAnnouncementProperty.getAnswerEdit();
        this.answerCFirstLine = emailAnnouncementProperty.getAnswerCFirstLine();
        this.answerEFirstLine = emailAnnouncementProperty.getAnswerEFirstLine();

        this.sendCoolDown = emailAnnouncementProperty.getSendCoolDown()*1000;
        this.host = emailAnnouncementProperty.getHost();
        InternetAddress from = new InternetAddress(emailConnectionProperty.getUsername(), false);

        purchaseCreateMessage = emailSender.createMimeMessage();
        purchaseCreateMessage.setFrom(from);
        purchaseCreateMessage.setSubject(emailAnnouncementProperty.getPurchaseCSubject(), "UTF-8");

        supplyEditMessage = emailSender.createMimeMessage();
        supplyEditMessage.setFrom(from);
        supplyEditMessage.setSubject(emailAnnouncementProperty.getSupplyESubject(), "UTF-8");

        answerCEMessage = emailSender.createMimeMessage();
        answerCEMessage.setFrom(from);
        answerCEMessage.setSubject(emailAnnouncementProperty.getAnswerSubject(), "UTF-8");
    }

    @Async
    public void sendMailPurchaseCreate(Purchase purchase, List<User> users)
    {
        if (purchaseCreate == 0 || users.size() == 0) return;

        while(busyPurchaseCreate)
        {
            try { Thread.sleep(SleepTime); }
            catch (InterruptedException e)
            {
                /*TODO логирование warning*/
            }
        }
        busyPurchaseCreate = true;

        String content = purchaseCFirstLine + "<br>";
        content = content + "<b>Название закупки:</b> " + purchase.getName() + "<br>";
        if (purchaseCDescription == 1)
        {
            content = content + "<b>Описание закупки:</b> " + purchase.getDescription() + "<br>";
        }
        if (purchaseCBudget == 1)
        {
            Long budget = purchase.getBudget();
            if (budget > 0) content = content + "<b>Бюджет закупки:</b> " + budget + "<br>";
        }
        content = content + "<b>Закупка доступна по адресу:</b> " + host + "/purchase/" + purchase.getId();
        Date nowDate = new Date();

        try
        {
            purchaseCreateMessage.setContent(content, "text/html; charset=UTF-8");
            purchaseCreateMessage.setSentDate(nowDate);
        }
        catch (MessagingException e)
        {
            /*TODO логирование warning*/
        }

        List<EmailParams> emailParamsList = new ArrayList<>(users.size());

        for (User user : users)
        {
            emailParamsList.add(new EmailParams(3, purchaseCreateMessage, user.getEmail(), nowDate));
        }

        busyPurchaseCreate = false;

        try { addToQueue(emailParamsList); }
        catch (MessagingException | InterruptedException e)
        {
            /*TODO логирование warning*/
        }
    }

    @Async
    public void sendMailSupplyEdit(Purchase purchase, SupplyEntity supplyEntity, User user)
    {
        if (supplyEdit == 0) return;

        while(busySupplyEdit)
        {
            try { Thread.sleep(SleepTime); }
            catch (InterruptedException e)
            {
                /*TODO логирование warning*/
            }
        }
        busySupplyEdit = true;

        String content = supplyEFirstLine + " “" + supplyEntity.getStatus()  + "“<br>";
        content = content + "<b>Название закупки:</b> " + purchase.getName() + "<br>";
        String result = supplyEntity.getResult();
        if(result.length() > 0) content = content + "<b>Результат рассмотрения:</b> " + result + "<br>";
        content = content + "<b>Закупка доступна по адресу:</b> " + host + "/purchase/" + purchase.getId();
        Date nowDate = new Date();

        try
        {
            supplyEditMessage.setContent(content, "text/html; charset=UTF-8");
            supplyEditMessage.setSentDate(nowDate);
        }
        catch (MessagingException e)
        {
            /*TODO логирование warning*/
        }

        EmailParams emailParams = new EmailParams(1, supplyEditMessage, user.getEmail(), nowDate);

        busySupplyEdit = false;

        try { addToQueue(emailParams); }
        catch (MessagingException | InterruptedException e)
        {
            /*TODO логирование warning*/
        }
    }

    @Async
    public void sendMailAnswerCreate(Purchase purchase, Question question, Answer answer, User user)
    {
        if (answerCreate == 0) return;

        while(busyAnswerCE)
        {
            try { Thread.sleep(SleepTime); }
            catch (InterruptedException e)
            {
                /*TODO логирование warning*/
            }
        }
        busyAnswerCE = true;

        String content = answerCFirstLine + "<br>";
        content = content + "<b>Название закупки:</b> " + purchase.getName() + "<br>";
        content = content + "<b>Тема вопроса:</b> " + question.getName() + "<br>";
        content = content + "<b>Текст вопроса:</b> " + question.getDescription() + "<br>";
        content = content + "<b>Текст ответа:</b> " + answer.getDescription() + "<br>";
        content = content + "<b>Закупка доступна по адресу:</b> " + host + "/purchase/" + purchase.getId();
        Date nowDate = new Date();

        try
        {
            answerCEMessage.setContent(content, "text/html; charset=UTF-8");
            answerCEMessage.setSentDate(nowDate);
        }
        catch (MessagingException e)
        {
            /*TODO логирование warning*/
        }

        EmailParams emailParams = new EmailParams(2, answerCEMessage, user.getEmail(), nowDate);

        busyAnswerCE = false;

        try { addToQueue(emailParams); }
        catch (MessagingException | InterruptedException e)
        {
            /*TODO логирование warning*/
        }
    }

    @Async
    public void sendMailAnswerEdit(Purchase purchase, Question question, Answer answer, User user)
    {
        if (answerEdit == 0) return;

        while(busyAnswerCE)
        {
            try { Thread.sleep(SleepTime); }
            catch (InterruptedException e)
            {
                /*TODO логирование warning*/
            }
        }
        busyAnswerCE = true;

        String content = answerEFirstLine + "<br>";
        content = content + "Название закупки: " + purchase.getName() + "<br>";
        content = content + "Тема вопроса: " + question.getName() + "<br>";
        content = content + "Текст вопроса: " + question.getDescription() + "<br>";
        content = content + "Текст ответа: " + answer.getDescription() + "<br>";
        content = content + "Закупка доступна по адресу: " + host + "/purchase/" + purchase.getId();
        Date nowDate = new Date();

        try
        {
            answerCEMessage.setContent(content, "text/html; charset=UTF-8");
            answerCEMessage.setSentDate(nowDate);
        }
        catch (MessagingException e)
        {
            /*TODO логирование warning*/
        }

        EmailParams emailParams = new EmailParams(2, answerCEMessage, user.getEmail(), nowDate);

        busyAnswerCE = false;

        try { addToQueue(emailParams); }
        catch (MessagingException | InterruptedException e)
        {
            /*TODO логирование warning*/
        }
    }

    @Async
    private void addToQueue(EmailParams emailParams) throws InterruptedException, MessagingException
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
    private void addToQueue(List<EmailParams> emailParamsList) throws InterruptedException, MessagingException
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
    private void beginSendMail() throws InterruptedException, MessagingException
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
        if(sendCoolDown > 0) Thread.sleep(sendCoolDown);
        beginSendMail();
    }

    private static final Comparator<EmailParams> EmailSendComparator = new Comparator<EmailParams>()
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