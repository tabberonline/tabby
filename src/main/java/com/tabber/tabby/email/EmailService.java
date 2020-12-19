package com.tabber.tabby.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        String escaped = HtmlUtils.htmlEscape(message);
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            mailMessage.setSubject(subject, "UTF-8");

            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom("tabberonline@gmail.com");
            helper.setTo(toEmail);
           // helper.setText(message, true);
           // helper.setText("<html><body><h1>Test h ye</h1><p>ggering deferred initialization of Spring Data</p></body></html>", true);
helper.setText(escaped);

//            var mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(toEmail);
//            mailMessage.setSubject(subject);
//            mailMessage.setText(escaped);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){

        }
    }
}
