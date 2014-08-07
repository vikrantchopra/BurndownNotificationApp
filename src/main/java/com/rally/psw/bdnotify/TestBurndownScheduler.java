package com.rally.psw.bdnotify;

import org.quartz.SchedulerException;

public class TestBurndownScheduler {

	public static void main(String[] args) throws SchedulerException {
		// TODO Auto-generated method stub
		System.out.println("Starting Main");
		BurndownSchedTrigger trigger = new BurndownSchedTrigger();
		
		trigger.triggerBurndownCheck();
	}

}
