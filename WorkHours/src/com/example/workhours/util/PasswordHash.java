package com.example.workhours.util;

import java.security.MessageDigest;

import android.util.Log;
/**
 * Auxilary class to help hash user password
 * */
public class PasswordHash {
	
	public static String hash(String pswd) {
		
		String hashedPassword = null;
		
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pswd.getBytes());
			//Get Hash bytes
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0; i < bytes.length; i++) {
				
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			//Get hashed password in hexadecimal form
			hashedPassword = sb.toString();
			
		} catch(Exception e) {
			
			Log.d("Error while hashing password", e.getMessage());
		}
		
		return hashedPassword;
		
	}

}
