package com.rally.psw.bdnotify;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class BurndownSchedTrigger {

	public void triggerBurndownCheck() throws SchedulerException {
		System.out.println("Running the trigger");
		JobDetail job = JobBuilder.newJob(CheckBurndownJob.class)
				.withIdentity("Burndown Scheduling Job", "group1").build();
		
		@SuppressWarnings("static-access")
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("dummyTrigger")
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(5)
								.repeatSecondlyForTotalCount(1, 5)).build();

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
	}
}
