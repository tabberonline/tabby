package com.tabber.tabby.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tabber.tabby.email.EmailService;
import com.tabber.tabby.entity.EmailEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.EmailsManager;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.EmailsRepository;
import com.tabber.tabby.service.EmailTabberProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailTabberProfileServiceImpl implements EmailTabberProfileService {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    UserResumeManager userManager;

    @Autowired
    EmailsManager emailsManager;

    @Autowired
    EmailsRepository emailsRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmailService emailService;

    private void sendMailWithTemplate(UserEntity userEntity, String toEmail) {

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            Map<String,Object> model =new HashMap<String,Object>();
            model.put("name",userEntity.getName());
            model.put("email",userEntity.getEmail());
            model.put("title",userEntity.getPortfolio().getTitle());
            model.put("description",userEntity.getPortfolio().getDescription());

            Context context = new Context();
            context.setVariables(model);

            mailMessage.setSubject("Visit "+ userEntity.getName() +"'s Tabber Profile", "UTF-8");
            String html = templateEngine.process("tabberProfile",context);
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, StandardCharsets.UTF_8.name());
            helper.setTo(toEmail);
            helper.setText(html, true);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){

        }
    }

    @Override
    public void sendTabbyProfileInEmail(Long userProfileId,String receiverEmail){
        UserEntity userEntity = userManager.findUserById(userProfileId);
        EmailEntity emailEntity = emailsManager.getEmailByProfileId(userProfileId);
        if(emailEntity == null) {
            emailEntity = new EmailEntity();
            emailEntity.setId(userProfileId);
            JsonNode array = objectMapper.createArrayNode().add(createEmailObject(receiverEmail));
            JsonNode emailObject = objectMapper.createObjectNode();
            ((ObjectNode)emailObject).set("data", array);
            emailEntity.setEmailData(emailObject);
        }
        else {
            JsonNode jsonNode = emailEntity.getEmailData();
            ((ArrayNode)jsonNode.get("data")).add(createEmailObject(receiverEmail));
            emailEntity.setEmailData(jsonNode);
        }
        emailsManager.evictEmailCacheValue(userProfileId);
        emailsRepository.saveAndFlush(emailEntity);
        sendMailWithTemplate(userEntity,receiverEmail);
    }

    private JsonNode createEmailObject(String emailTo){
        JsonNode emailObject = objectMapper.createObjectNode();
        ((ObjectNode)emailObject).put("email", emailTo);
        ((ObjectNode)emailObject).put("date", getCurrentDateInUTC());
        return emailObject;
    }

    private String getCurrentDateInUTC(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

}
