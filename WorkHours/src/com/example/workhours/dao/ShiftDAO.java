package com.example.workhours.dao;

import java.util.List;

import com.example.workhours.entities.Shift;

public interface ShiftDAO {
	
	public void addShift(Shift shift);
	public Shift getShift(Integer sID);
	public List<Shift> getShifts();
	public void deleteShift(Integer sID);
	public void updateShift(Integer sID, Shift shift);
	public void open();
	public void close();


}
