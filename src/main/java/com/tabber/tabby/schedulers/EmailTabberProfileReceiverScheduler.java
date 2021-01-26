package com.tabber.tabby.schedulers;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class EmailTabberProfileReceiverScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("Hello from Job emailingProfileReceiverJob!");
    }
}
