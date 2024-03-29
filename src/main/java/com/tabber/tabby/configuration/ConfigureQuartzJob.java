package com.tabber.tabby.configuration;

import com.tabber.tabby.schedulers.EmailTabberProfileReceiverScheduler;
import com.tabber.tabby.schedulers.ExpiringTrackingIdScheduler;
import com.tabber.tabby.schedulers.IncrementViewsScheduler;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureQuartzJob {

    @Autowired @Qualifier("emailJob")
    JobDetail emailJobDetail;

    @Autowired @Qualifier("viewsInDB")
    JobDetail viewsInDBJobDetail;

    @Autowired @Qualifier("deleteTrackingIds")
    JobDetail deleteTrackingIdsJobDetail;

    @Bean(name = "emailJob")
    public JobDetail emailingProfileReceiverJob() {
        JobDetail emailingProfileReceiverJob = JobBuilder.newJob(EmailTabberProfileReceiverScheduler.class)
                .withIdentity("emailingProfileReceiverJob")
                .storeDurably().build();
        return emailingProfileReceiverJob;
    }

    @Bean
    public Trigger emailJobTrigger() {
        return TriggerBuilder.newTrigger().forJob(emailJobDetail)
                .withIdentity("emailJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")) // every half hour
                .build();
    }

    @Bean(name = "viewsInDB")
    public JobDetail incrementViews() {
        JobDetail incrementViews = JobBuilder.newJob(IncrementViewsScheduler.class)
                .withIdentity("incrementViews")
                .storeDurably().build();
        return incrementViews;
    }

    @Bean
    public Trigger incrementViewsJobTrigger() {
        return TriggerBuilder.newTrigger().forJob(viewsInDBJobDetail)
                .withIdentity("incrementViewsJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/6 ? * * *")) // every six hour
                .build();
    }

    @Bean(name = "deleteTrackingIds")
    public JobDetail expiringTrackingId() {
        JobDetail expiringTrackingId = JobBuilder.newJob(ExpiringTrackingIdScheduler.class)
                .withIdentity("expiringTrackingId")
                .storeDurably().build();
        return expiringTrackingId;
    }

    @Bean
    public Trigger expiringTrackingIdTrigger() {
        return TriggerBuilder.newTrigger().forJob(deleteTrackingIdsJobDetail)
                .withIdentity("expiringTrackingIdTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 ? * * *")) // every hour
                .build();
    }

}