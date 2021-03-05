package com.tabber.tabby.email;

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
    private JavaMailSender javaMailSender;

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        logger.log(Level.INFO,"Sending email");
        try {
            mailMessage.setSubject(subject, "UTF-8");
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true,StandardCharsets.UTF_8.name());
            helper.setFrom("tabberonline@gmail.com");
            helper.setTo(toEmail);
            helper.setText(message);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){
            logger.log(Level.WARNING,"Failed in sending email on contact page, due to "+ex.toString());
        }
    }


}