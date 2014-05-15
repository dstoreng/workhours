package com.example.workhours.dao;

import java.util.List;

import com.example.workhours.entities.User;

public interface UserDAO {
	
	public void addUser(User user);
	public void updateUser(User user);
	public void dropUser();
	public List<User> getUsers();
	public User getUser(String email, String passhash);
	public void open();
	public void close();

}
