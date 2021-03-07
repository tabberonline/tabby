package com.tabber.tabby.email;

import com.tabber.tabby.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class EmailService {

    @Autowired
    private AWSService awsService;

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendMail(String toEmail, String subject, String message, String email) {
        logger.log(Level.INFO,"Sending email");
        try {
            String emailPart = "EMAIL : ".concat(email).concat("\n");
            String subjectPart = "SUBJECT : ".concat(subject).concat("\n");
            String messagePart = "MESSAGE : ".concat(message).concat("\n");
            String textPart = emailPart + subjectPart + messagePart;
            awsService.sendSESEmail(toEmail,textPart,null,subject,null,null);
        }
        catch (Exception ex){
            logger.log(Level.WARNING,"Failed in sending email on contact page, due to "+ex.toString());
        }
    }


}