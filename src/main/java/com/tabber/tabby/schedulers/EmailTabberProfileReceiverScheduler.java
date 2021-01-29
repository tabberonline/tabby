package com.tabber.tabby.schedulers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csvreader.CsvWriter;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.manager.RedisServiceManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class EmailTabberProfileReceiverScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        aws();
    }

    private final String accessKey="AKIA5Y57L2I37TQ2AKEA";
    private final String secretKey ="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";
    private AmazonS3 s3client;

    @Autowired
    RedisServiceManager redisServiceManager;

    public void aws(){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            CsvWriter writer = new CsvWriter(stream, '\n', Charset
                    .forName("ISO-8859-1"));

            Integer chainCount = Integer.parseInt(redisServiceManager.getValueForKey("COUNT_EMAIL_KEY"));
            for(int i=1;i<=chainCount; i++){
                String emailChainKey = TabbyConstants.EMAIL_KEY+"_"+i;
                List<String> emailChainList = redisServiceManager.lrange(emailChainKey,0,TabbyConstants.MAX_EMAILS_PER_KEY-1);
                for (String email:emailChainList)
                    writer.write(email);

            }

            AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            this.s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("title", "Receiver Emails");

            InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
            PutObjectRequest request = new PutObjectRequest("elasticbeanstalk-us-east-2-946904748599", "test", inputStream,metadata);
            stream.close();

            request.setMetadata(metadata);
            this.s3client.putObject(request);
        }
        catch (Exception e){}
    }
}
