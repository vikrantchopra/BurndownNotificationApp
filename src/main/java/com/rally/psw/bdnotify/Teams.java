package com.rally.psw.bdnotify;

public enum Teams {

	
	MOBILEARCHITECTURE("2508024826", "vikrant.chopra@perceptivesoftware.com"),
	MOBILECAPTURE("9332818014", "vikrant.chopra@perceptivesoftware.com"),
	MOBILEVIEWER("2720146656", "vikrant.chopra@perceptivesoftware.com"),
	WARRIORS("9618552669", "vikrant.chopra@perceptivesoftware.com"),
	KNIGHTRIDERS("3874483234", "vikrant.chopra@perceptivesoftware.com"),
	IHE("2720145837", "vikrant.chopra@perceptivesoftware.com"),
	ROYALS("7418464221", "vikrant.chopra@perceptivesoftware.com"),
	DAX ("3574043643", "vikrant.chopra@perceptivesoftware.com"),
	DAREDEVILS("2508024112", "vikrant.chopra@perceptivesoftware.com"),
	SUPERKINGS("4159399373", "vikrant.chopra@perceptivesoftware.com"),
	ALCHEMIST("6869746225", "vikrant.chopra@perceptivesoftware.com");
	
	private final String project;
	private final String scrumMaster;
	
	Teams(String project, String scrumMaster) {
		this.project = project;
		this.scrumMaster = scrumMaster;
	}
	
	public String project() {
		return project;
	}
	
	public String scrumMaster() {
		return scrumMaster;
	}
}
