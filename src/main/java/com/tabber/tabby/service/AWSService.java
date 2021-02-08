package com.tabber.tabby.service;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.sun.istack.ByteArrayDataSource;
import com.tabber.tabby.entity.UserEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AWSService {

    private AmazonS3 s3client;

    @Async
    public void sendSESEmail(String toEmail, String HTML, String subject, MultipartFile multipartFile, UserEntity userEntity){
        try {
        String fromEmail = "communications@tabber.online";
        Session session = Session.getDefaultInstance(new Properties());
        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Add subject, from and to lines.
        message.setSubject(subject, "UTF-8");
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        // Create a wrapper for the HTML and text parts.
        MimeBodyPart wrap = new MimeBodyPart();
        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");
        // Define the text part.
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent("THIS IS TEXT TEST", "text/plain; charset=UTF-8");
        // Define the HTML part.
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(HTML,"text/html; charset=UTF-8");
        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(textPart);
        msg_body.addBodyPart(htmlPart);
        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);

        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");
        // Add the parent container to the message.
        message.setContent(msg);
        // Add the multipart/alternative part to the message.
        msg.addBodyPart(wrap);

        // Define the attachment
        MimeBodyPart att = new MimeBodyPart();

        String mimeType = FileTypeMap.getDefaultFileTypeMap().getContentType(multipartFile.getOriginalFilename());
        DataSource dataSource = new ByteArrayDataSource(multipartFile.getBytes(), mimeType);
        att.setDataHandler(new DataHandler(dataSource));
        att.setFileName(userEntity.getName().trim()+".pdf");

        // Add the attachment to the message.
        msg.addBodyPart(att);
        AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.US_EAST_2)
                    .build();
            // Send the email.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        RawMessage rawMessage =new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

        SendRawEmailRequest rawEmailRequest =new SendRawEmailRequest(rawMessage);
        client.sendRawEmail(rawEmailRequest);
        System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }

    public void uploadOnS3(String bucketName, String bucketKey, InputStream inputStream, ObjectMetadata metaData){
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.US_EAST_2)
                .build();
        PutObjectRequest request = new PutObjectRequest("tabbybucket1", bucketKey, inputStream,metaData);
        this.s3client.putObject(request);
    }
}
