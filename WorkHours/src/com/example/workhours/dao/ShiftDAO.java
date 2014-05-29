package com.example.workhours.dao;

import java.util.List;

import com.example.workhours.entities.Shift;

public interface ShiftDAO {
	
	public void addShift(Shift shift);
	public Shift getShift(Integer sID);
	
	/**
	 * Returns a list of shifts sorted by start date.
	 * @return ArrayList
	 */
	public List<Shift> getShifts();
	public List<Shift> getSchedule();
	public void deleteShift(Integer sID);
	public void updateShift(Integer sID, Shift shift);
	public void open();
	public void close();


}
