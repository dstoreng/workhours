package com.example.workhours.entities;

import com.example.workhours.util.PasswordHash;

public class User {
	
	private String name;
	private String email;
	private String employerEmail;
	private String passHash;
	private double hourlyWage;
	private double tax;
	private int dueDate;
	private String perPay;
	 
	
	public User() {}
	
	public User(String name, String email, String password) {
		
		this(name, email, password, "", 0.0, 0.0, 0, "");
	}
	
	public User(String name, String email, String password, String empEmail, double hWage, double tax, int due, String perPay) {
		
		this.name     = name;
		this.email    = email;
		employerEmail = empEmail;
		hourlyWage    = hWage;
		this.tax      = tax;
		this.passHash = PasswordHash.hash(password);
		dueDate       = due;
		this.setPerPay(perPay);

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
	
	public boolean isValidEmployerEmail(){
		return ((employerEmail.trim().equals("")) || (employerEmail == null)) ? false : true;
	}

	public int getScheduleDue() {
		return dueDate;
	}

	public void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}
	
	public double getTaxRate(double income) {
		
		if(income > 0 && income <= 14000) {
			
			tax = 0.105;
		} else if (income > 140000 && income <= 48000) {
			
			tax = 0.175;
		} else if (income > 48000 && income <= 70000) {
			
			tax = 0.30;
		} else if(income > 70000) {
			
			tax = 0.330;
		} else {
			
			tax = 0.45;
		}
		
		return tax;
	}
	
	public double calculateEarnings(double hoursWorked) {
		
		return (hoursWorked*hourlyWage) - (hoursWorked*hourlyWage*tax);
		
	}

	public String getPerPay() {
		return perPay;
	}

	public void setPerPay(String perPay) {
		this.perPay = perPay;
	}

}
