package com.rally.psw.bdnotify;

import java.net.URI;
import java.net.URISyntaxException;

import com.rallydev.rest.RallyRestApi;

public class SetupRallyService {
	private static String server = "https://rally1.rallydev.com";
	private static String username = "rd.operations@imagenow.com";
	private static String password = "ImageNow!";
	
	String appName = "Reading Rally Data";
	
	public static RallyRestApi getRallyRestApi() throws URISyntaxException {
		return new RallyRestApi(new URI(server), username, password);
	}
}
