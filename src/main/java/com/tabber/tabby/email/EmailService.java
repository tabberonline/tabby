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

    public void sendMailWithTemplate(UserEntity userEntity, String toEmail) {

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
            String html = templateEngine.process("sample",context);
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true,StandardCharsets.UTF_8.name());
            helper.setTo(toEmail);
            helper.setText(html, true);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){

        }
    }

    public void sendTabbyProfileInEmail(Long userProfileId,String receiverEmail){
        UserEntity userEntity = userManager.findUserById(userProfileId);
        EmailEntity emailEntity = emailsManager.getEmailByProfileId(userProfileId);
        if(emailEntity == null) {
            emailEntity = new EmailEntity();
            emailEntity.setId(userProfileId);
            JsonNode jsonNode = JsonNodeFactory.instance.objectNode();
            ArrayList<JSONObject> emailList = new ArrayList();
            emailList.add(createEmailObject(receiverEmail));
            ((ObjectNode)jsonNode).set("data", objectMapper.valueToTree(emailList));
            emailEntity.setEmailData(jsonNode);
        }
        else {
            JsonNode jsonNode = emailEntity.getEmailData();
            ArrayList<JSONObject> emailList = objectMapper.convertValue(jsonNode.get("data"),ArrayList.class);
            emailList.add(createEmailObject(receiverEmail));
            ((ObjectNode)jsonNode).set("data", objectMapper.valueToTree(emailList));
            emailEntity.setEmailData(jsonNode);
        }
        emailsManager.evictEmailCacheValue(userProfileId);
        emailsRepository.saveAndFlush(emailEntity);
        sendMailWithTemplate(userEntity,receiverEmail);
    }

    private JSONObject createEmailObject(String emailTo){
        JSONObject emailObject = new JSONObject();
        emailObject.put("email",emailTo);
        emailObject.put("date",new Date());
        return emailObject;
    }
}