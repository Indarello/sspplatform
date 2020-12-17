package com.ssp.platform.service.impl;

import com.ssp.platform.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
@EnableAsync
public class MailServiceImpl
{
    private final MimeMessage message;
    @Autowired
    MailServiceImpl() throws MessagingException
    {
        //TODO: вынести в properties

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("bfgbfashtyndspgdfg@gmail.com", "bgf8912NLA_");
            }
        });

        message = new MimeMessage(session);
        message.setFrom(new InternetAddress("bfgbfashtyndspgdfg@gmail.com", false));
    }

    @Async
    public void sendMailPurchase(String subject, String text, List<User> users, Date date) throws MessagingException, IOException
    {
        for (User user : users)
        {
            message.setSubject(subject, "UTF-8");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setContent(text, "text/html; charset=UTF-8");
            message.setSentDate(date);
            Transport.send(message);
        }
    }

}