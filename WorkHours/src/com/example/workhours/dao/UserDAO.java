package com.example.workhours.dao;

import com.example.workhours.entities.User;

public interface UserDAO {
	
	public void addUser(User user);
	public void updateUser(User user);
	public void dropUser();
	public User getUser();
	public void open();
	public void close();

}
