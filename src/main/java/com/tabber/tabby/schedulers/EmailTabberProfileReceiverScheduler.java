package com.tabber.tabby.schedulers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.csvreader.CsvWriter;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.service.AWSService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailTabberProfileReceiverScheduler implements Job {

    private static final Logger logger = Logger.getLogger(EmailTabberProfileReceiverScheduler.class.getName());

    @Override
    public void execute(JobExecutionContext context) {
        aws();
    }

    @Autowired
    AWSService awsService;

    @Autowired
    RedisServiceManager redisServiceManager;

    private AmazonS3 s3client;
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
            writer.endRecord();
            writer.close();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("csv");
            metadata.addUserMetadata("yyyyMMddHHmm emails key", "date coded key");

            InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
            stream.close();

            //String dateKey = DateUtil.getDateKey()+"emails.csv";
            // replacing same name to keep min storage, replace later with above
            String dateKey = "emails.csv";
            awsService.uploadOnS3("tabbybucket1",dateKey,inputStream,metadata);

        }
        catch (Exception e){
            logger.log(Level.WARNING,"Failed uploading on S3 due to exception:"+e.toString());
        }
    }
}
