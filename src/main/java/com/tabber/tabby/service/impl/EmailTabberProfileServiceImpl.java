package com.tabber.tabby.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.dto.EmailHistoryResponse;
import com.tabber.tabby.dto.StatusWiseResponse;
import com.tabber.tabby.entity.EmailEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.enums.ResponseStatus;
import com.tabber.tabby.exceptions.BadRequestException;
import com.tabber.tabby.manager.EmailsManager;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.EmailsRepository;
import com.tabber.tabby.service.AWSService;
import com.tabber.tabby.service.EmailTabberProfileService;
import com.tabber.tabby.service.ReceiverEmailListRedisService;
import com.tabber.tabby.utils.DateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
    ReceiverEmailListRedisService receiverEmailListRedisService;

    @Autowired
    AWSService awsService;

    private String getHTMLwithTemplate(UserEntity userEntity, String toEmail, MultipartFile multipartFile) {

//        MimeMessage mailMessage = javaMailSender.createMimeMessage();
//        try {
            Map<String,Object> model =new HashMap<String,Object>();
            model.put("name",userEntity.getName());
            model.put("email",userEntity.getEmail());
            model.put("title",userEntity.getPortfolio().getTitle());
            model.put("description",userEntity.getPortfolio().getDescription());

            Context context = new Context();
            context.setVariables(model);

            String subject ="Visit "+ userEntity.getName() +"'s Tabber Profile";
            //mailMessage.setSubject(subject, "UTF-8");
            String html = templateEngine.process("tabberProfile",context);
            return html;
//            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, StandardCharsets.UTF_8.name());
//            helper.setTo(toEmail);
//            helper.setText(html, true);
//            helper.addAttachment("userResume.pdf",multipartFile);
            //javaMailSender.send(mailMessage);
//        }
//        catch (Exception ex){
//
//        }
    }

    @Override
    public StatusWiseResponse sendTabbyProfileInEmail(Long userProfileId, String receiverEmail, MultipartFile multipartFile) throws Exception{
        UserEntity userEntity = userManager.findUserById(userProfileId);
        EmailEntity emailEntity = emailsManager.getEmailByProfileId(userProfileId);
        StatusWiseResponse statusWiseResponse= new StatusWiseResponse().toBuilder()
                .status(ResponseStatus.success.name())
                .message("Email Successfully sent")
                .build();
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
            if(checkIfEmailSendingLimitReached(jsonNode)) {
                statusWiseResponse= new StatusWiseResponse().toBuilder()
                        .status(ResponseStatus.failure.name())
                        .message("Email sending limit of " + TabbyConstants.EMAIL_SENDING_LIMIT + " emails reached")
                        .build();
                return statusWiseResponse;
            }
            if((jsonNode.get("data")).size()>=TabbyConstants.EMAIL_HISTORY_STORING_LIMIT){
                ((ArrayNode)jsonNode.get("data")).remove(0);
            }
            ((ArrayNode)jsonNode.get("data")).add(createEmailObject(receiverEmail));
            emailEntity.setEmailData(jsonNode);
        }
        emailsManager.evictEmailCacheValue(userProfileId);
        emailsRepository.saveAndFlush(emailEntity);
        receiverEmailListRedisService.addEmailToRedisCachedList(receiverEmail);
        String html = getHTMLwithTemplate(userEntity,receiverEmail,multipartFile);
        String subject ="Visit "+ userEntity.getName() +"'s Tabber Profile";
        awsService.sendSESEmail(receiverEmail,html,subject,multipartFile,userEntity);
        return statusWiseResponse;
    }

    private Boolean checkIfEmailSendingLimitReached(JsonNode jsonNodeArray){
        ArrayList<JSONObject> emailHistory = objectMapper.convertValue(jsonNodeArray.get("data"),ArrayList.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // EMAIL_HISTORY_STORING_LIMIT should always be greater than EMAIL_SENDING_LIMIT, otherwise this check never works
        if(emailHistory.size() - 1 - TabbyConstants.EMAIL_SENDING_LIMIT < 0)
            return false;
        try {
            Date lastEmailDate = sdf.parse(objectMapper.convertValue(emailHistory.get(emailHistory.size() - 1),
                    LinkedHashMap.class).get("date").toString());
            Date dateToCompare = sdf.parse(objectMapper.convertValue(emailHistory.get(emailHistory.size() - 1 - TabbyConstants.EMAIL_SENDING_LIMIT),
                    LinkedHashMap.class).get("date").toString());
            Date dateToday= sdf.parse(DateUtil.getCurrentDateInUTC());
            if(lastEmailDate.equals(dateToCompare) && dateToday.equals(lastEmailDate))
                return true;
        }
        catch (Exception ex){}

        return false;
    }

    @Override
    public EmailHistoryResponse getPaginatedEmailHistory(Integer pageNo, Integer itemsPerPage, Long userProfileId){
        EmailEntity emailEntity = emailsManager.getEmailByProfileId(userProfileId);
        if(emailEntity == null){
            return new EmailHistoryResponse().toBuilder()
                    .totalItems(0)
                    .pageNo(pageNo)
                    .itemsPerPage(itemsPerPage)
                    .mailHistory(new ArrayList<>())
                    .build();
        }
        JsonNode jsonNodeArray = emailEntity.getEmailData().get("data");
        ArrayList<JSONObject> emailHistory = objectMapper.convertValue(jsonNodeArray,ArrayList.class);
        List<JSONObject> emailSlicedHistory = new ArrayList<>();
        if((pageNo-1)*itemsPerPage <= emailHistory.size()) {
            Integer toItemNumber = Math.min((pageNo - 1) * itemsPerPage + itemsPerPage, emailHistory.size());
            emailSlicedHistory = emailHistory.subList((pageNo - 1) * itemsPerPage, toItemNumber);
        }
        EmailHistoryResponse emailHistoryResponse = new EmailHistoryResponse().toBuilder()
                .totalItems(emailHistory.size())
                .pageNo(pageNo)
                .itemsPerPage(itemsPerPage)
                .mailHistory(emailSlicedHistory)
                .build();
        return emailHistoryResponse;

    }


    private JsonNode createEmailObject(String emailTo){
        JsonNode emailObject = objectMapper.createObjectNode();
        ((ObjectNode)emailObject).put("email", emailTo);
        ((ObjectNode)emailObject).put("date", DateUtil.getCurrentDateInUTC());
        return emailObject;
    }


}
