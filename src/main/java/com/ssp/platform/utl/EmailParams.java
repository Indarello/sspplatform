package com.ssp.platform.utl;

import lombok.Data;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Data
public class EmailParams
{
    private int priority;
    private MimeMessage message;
    private String email;
    private Date date;

    public EmailParams(int priority, MimeMessage message, String email, Date date)
    {
        this.priority = priority;
        this.message = message;
        this.email = email;
        this.date = date;
    }
}
