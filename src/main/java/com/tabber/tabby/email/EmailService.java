package com.tabber.tabby.email;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tabber.tabby.entity.EmailEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.EmailsManager;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.EmailsRepository;
import com.tabber.tabby.respository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            mailMessage.setSubject(subject, "UTF-8");
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true,StandardCharsets.UTF_8.name());
            helper.setFrom("tabberonline@gmail.com");
            helper.setTo(toEmail);
            helper.setText(message);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){
        }
    }


}