package com.example.workhours.entities;

import android.util.Log;

import com.example.workhours.util.PasswordHash;

public class User {
	
	private String name;
	private String email;
	private String employerEmail;
	private String passHash;
	private double hourlyWage;
	private double tax;
	 
	
	public User() {}
	
	public User(String name, String email, String password) {
		
		this(name, email, password, "", 0.0, 0.0);
	}
	
	public User(String name, String email, String password, String empEmail, double hWage, double tax) {
		
		this.name  = name;
		this.email = email;
		employerEmail = empEmail;
		hourlyWage = hWage;
		this.tax = tax;
		
		Log.d("PASSWORD IN CLASS:", password);
		
		this.passHash = PasswordHash.hash(password);

	}
	
	public String getName() {
		
		return name;
	}
	public void setName(String name) {
		
		this.name = name;
	}
	public String getEmail() {
		
		return email;
	}
	public void setEmail(String email) {
		
		this.email = email;
	}
	public String getPassHash() {
		
		return passHash;
	}
	public void setPassHash(String passHash) {
		
		this.passHash = passHash;
	}
	public double getHourlyWage() {
		
		return hourlyWage;
	}
	public void setHourlyWage(double hourlyWage) {
		
		this.hourlyWage = hourlyWage;
	}
	public double getTax() {
		
		return tax;
	}
	public void setTax(double tax) {
		
		this.tax = tax;
	}
	public String getEmployerEmail() {
		
		return employerEmail;
	}
	public void setEmployerEmail(String employerEmail) {
		
		this.employerEmail = employerEmail;
	}
	
	public String toString() {
		
		return name + ";" + email + ";" + passHash + ";" + employerEmail + ";" + hourlyWage + ";" + tax;
	}
	
	

}
