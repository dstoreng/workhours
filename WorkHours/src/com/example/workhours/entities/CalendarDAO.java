package com.example.workhours.entities;


import java.util.List;
public interface CalendarDAO {
	
	/**
	 * Add new event to android calendar.
	 * 
	 * Id is stored @List<Long> in DAOImpl
	 */
	public void addCalendarEvent(Shift shift);
	
	/**
	 * Returns the unique id of all added events.
	 */
	public List<Long> getAddedEventsId();
}
