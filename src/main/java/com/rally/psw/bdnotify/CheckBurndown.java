package com.rally.psw.bdnotify;

import java.io.IOException;

public class CheckBurndown {

	public CheckBurndown() throws IOException {
		System.out.println("Checking Team Burndowns");
		System.out.println("-----------------------------------------------------");
		for(Teams team : Teams.values()) {
			IterationBurndown itrBD = new IterationBurndown(team.project(), team);
			
			if(itrBD.isBurndownViolation()) {
				System.out.println("Sending email to ScrumMaster");
				SendEmail email = new SendEmail(team.scrumMaster());
				email.sendMail();
			}
			
			itrBD.calculateBurndownSlope();
			System.out.println("-----------------------------------------------------");
				
		}
	}
}
