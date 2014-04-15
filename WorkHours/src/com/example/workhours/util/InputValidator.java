package com.example.workhours.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	/*
	public InputValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}*/
	
	public static boolean email(final String hex) {
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
	
	public static int password(final String pass) {
		
		return pass.length();
	}
}
