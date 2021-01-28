package com.tabber.tabby.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csvreader.CsvWriter;
import com.tabber.tabby.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.Charset;

@RestController
public class HealthController {

    @Autowired
    EmailService emailService;
    @GetMapping(value = "ping")
    public ResponseEntity<String> ping(){

            aws();

        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    private AmazonS3 s3client;

    private final String accessKey="AKIA5Y57L2I37TQ2AKEA";
    private final String secretKey ="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";

    private void aws(){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            CsvWriter writer = new CsvWriter(stream, '\n', Charset
                    .forName("ISO-8859-1"));
            writer.write("tabberonline@gmail.com");
            writer.write("tabberonline@gmail.com");
            writer.endRecord();
            writer.close();

            AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            this.s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("title", "someTitle");

            InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
            PutObjectRequest request = new PutObjectRequest("elasticbeanstalk-us-east-2-946904748599", "test", inputStream,metadata);
            stream.close();

            request.setMetadata(metadata);
            this.s3client.putObject(request);
        }
        catch (Exception e){}
    }
}
