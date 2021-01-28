package com.tabber.tabby.configuration;

import com.tabber.tabby.schedulers.EmailTabberProfileReceiverScheduler;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureQuartzJob {

    @Bean
    public JobDetail emailingProfileReceiverJob() {
        JobDetail emailingProfileReceiverJob = JobBuilder.newJob(EmailTabberProfileReceiverScheduler.class).withIdentity("emailingProfileReceiverJob")
                .storeDurably().build();
        return emailingProfileReceiverJob;
    }

    @Bean
    public Trigger emailJobTrigger(JobDetail emailingProfileReceiverJob) {

        return TriggerBuilder.newTrigger().forJob(emailingProfileReceiverJob)
                .withIdentity("emailJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ? *")) // everyday
                .build();
    }
}
