package com.example.workhours.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String PASSWORD_PATTERN = 
            "((?=.*\\d)(?=.*[a-z]).{8,20})";
	
//	"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

	public static boolean email(final String hex) {
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
	
	public static boolean passwordStrength(final String passw) {
		
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(passw);
		matcher = pattern.matcher(passw);
		return matcher.matches();
	}
}
