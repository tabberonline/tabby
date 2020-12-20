package com.tabber.tabby.email;

import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    UserRepository userRepository;

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
            UserEntity userEntity=userRepository.getTopByUserId(1l);
            sendMailWithTemplate(userEntity);
        }
        catch (Exception ex){
        }
    }

    public void sendMailWithTemplate(UserEntity userEntity) {

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
            helper.setTo("tabberonline@gmail.com");
            helper.setText(html, true);
            javaMailSender.send(mailMessage);
        }
        catch (Exception ex){

        }
    }
}