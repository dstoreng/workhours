package com.example.workhours.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long[] list;
	private int index;
	private int capasity = 100;
	
	
	public CustomObject(){
		index = 0;
		list = new Long[capasity];
	}
	
	public void add(Long item){
		if(index < capasity)
		{
			list[index] = item;
			index++;
		}else
		{
			capasity = capasity * 2;
			Long[] tmp = new Long[capasity];
			for(int i = 0; i < index; i++){
				tmp[i] = list[i];
			}
			list = tmp;
		}
	}
	
	/**
	 * @return 
	 * 
	 * Return in ArrayList format
	 */
	public List<Long> getShiftId(){
		List<Long> shifts = new ArrayList<Long>();
		
		for(int i = 0; i < index; i++){
			shifts.add(list[i]);
		}
		
		return shifts;
	}
	
	
}
