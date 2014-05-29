package com.example.workhours.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.workhours.entities.Shift;
import com.example.workhours.util.ShiftOpenHelper;

public class ShiftDAOImpl implements ShiftDAO {
	
	private SQLiteDatabase database;
	private List<Shift> shifts = new ArrayList<Shift>();
	private ShiftOpenHelper dbHelper;
	
	private String[] allColumns = 
		{
			ShiftOpenHelper.COLUMN_SHIFT_ID,
			ShiftOpenHelper.COLUMN_USER_ID,
			ShiftOpenHelper.COLUMN_T_FROM,
			ShiftOpenHelper.COLUMN_T_TO,
			ShiftOpenHelper.COLUMN_NOTIFY,
			ShiftOpenHelper.COLUMN_REPEAT,
			ShiftOpenHelper.COLUMN_W_REPEAT,
			ShiftOpenHelper.COLUMN_M_REPEAT,
			ShiftOpenHelper.COLUMN_WORKED,
			ShiftOpenHelper.COLUMN_REPEAT_COUNT
		};
	
	public ShiftDAOImpl(Context context) {
		
		dbHelper = new ShiftOpenHelper(context);
	}
	
	@Override
	public void open() throws SQLException {
		
		database = dbHelper.getWritableDatabase();
	}

	@Override
	public void addShift(Shift shift) {
		
		ContentValues values = new ContentValues();
		
		values.put(ShiftOpenHelper.COLUMN_SHIFT_ID, shift.getId());
		values.put(ShiftOpenHelper.COLUMN_USER_ID, shift.getUId());
		values.put(ShiftOpenHelper.COLUMN_T_FROM, shift.getFromFormatted());
		values.put(ShiftOpenHelper.COLUMN_T_TO, shift.getToFormatted());
		values.put(ShiftOpenHelper.COLUMN_NOTIFY, shift.isNotify());
		values.put(ShiftOpenHelper.COLUMN_REPEAT, shift.isRepeat());
		values.put(ShiftOpenHelper.COLUMN_W_REPEAT, shift.isRepeatWeekly());
		values.put(ShiftOpenHelper.COLUMN_M_REPEAT, shift.isRepeatMonthly());
		values.put(ShiftOpenHelper.COLUMN_WORKED, shift.isWorked());
	//	values.put(ShiftOpenHelper.COLUMN_WORKED, shift.getRepeatCount());
		values.put(ShiftOpenHelper.COLUMN_REPEAT_COUNT, 1);
		
		database.insert(ShiftOpenHelper.TABLE_SHIFT, null, values);
	}

	@Override
	public Shift getShift(Integer sID) {
		
		Shift shift;
		
		Cursor cursor = database.query (
										ShiftOpenHelper.TABLE_SHIFT,
										allColumns,
										" sID ='" + sID + "'",
										null,
										null,
										null,
										null
									 	);
		
		if(cursor != null)
			cursor.moveToFirst();
		
		shift = cursorToShift(cursor);
		
		if(cursor != null && !cursor.isClosed())
			cursor.close();
		
		return shift;
	}

	@Override
	public List<Shift> getShifts() {
		
		Shift shift;
		
		Cursor cursor = database.query(ShiftOpenHelper.TABLE_SHIFT,
				allColumns, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();	
		
		while(!cursor.isAfterLast()) {
			
			shift = cursorToShift(cursor);
			shifts.add(shift);
			cursor.moveToNext();
		}
		
		if(cursor != null && !cursor.isClosed())
		    cursor.close();
		
		Collections.sort(shifts);
		
		Log.d("DB GETSHIFTS","returning a list with size " + shifts.size());
		return shifts;
	}
	
	@Override
	public List<Shift> getSchedule() {
		Shift shift;
		List<Shift> elems = new ArrayList<Shift>();
		Cursor cursor = database.query(ShiftOpenHelper.TABLE_SHIFT,
														allColumns, 
														null, 
														null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();	
		
		while(!cursor.isAfterLast()) {
			
			shift = cursorToShift(cursor);
			if(shift.getTo().isAfter(DateTime.now()))
				elems.add(shift);
			cursor.moveToNext();
		}
		
		if(cursor != null && !cursor.isClosed())
		    cursor.close();
		
		Collections.sort(elems);		
		
		
		Log.d("DB GETSHIFTS","returning a list with size " + elems.size());
		return elems;
	}

	@Override
	public void deleteShift(Integer sID) {
		
		database.delete(
						ShiftOpenHelper.TABLE_SHIFT,
						" sID ='" + sID + "'",
						null
						);
	}

	@Override
	public void updateShift(Integer sID, Shift shift) {
		
		ContentValues values = new ContentValues();
		values.put(ShiftOpenHelper.COLUMN_SHIFT_ID, shift.getId());
		values.put(ShiftOpenHelper.COLUMN_USER_ID, shift.getUId());
		values.put(ShiftOpenHelper.COLUMN_T_FROM, shift.getFromFormatted());
		values.put(ShiftOpenHelper.COLUMN_T_TO, shift.getToFormatted());
		values.put(ShiftOpenHelper.COLUMN_NOTIFY, shift.isNotify());
		values.put(ShiftOpenHelper.COLUMN_REPEAT, shift.isRepeat());
		values.put(ShiftOpenHelper.COLUMN_W_REPEAT, shift.isRepeatWeekly());
		values.put(ShiftOpenHelper.COLUMN_M_REPEAT, shift.isRepeatMonthly());
		values.put(ShiftOpenHelper.COLUMN_WORKED, shift.isWorked());
	//	values.put(ShiftOpenHelper.COLUMN_WORKED, shift.getRepeatCount());
		values.put(ShiftOpenHelper.COLUMN_REPEAT_COUNT, 1);
		
		database.update(
						ShiftOpenHelper.TABLE_SHIFT,
						values,
						" sID ='" + sID + "'",
						null
						);
	}

	@Override
	public void close() {
		dbHelper.close();
		
	}
	
	private Shift cursorToShift(Cursor cursor) {
		
		Shift shift = new Shift(false);
		
		shift.setId(cursor.getInt(0));
		shift.setUId(cursor.getString(1));
		shift.parseFrom(cursor.getString(2));
		shift.parseTo(cursor.getString(3));
		shift.setNotify((cursor.getInt(4) == 1) ? true : false);
		shift.setRepeat((cursor.getInt(5) == 1) ? true : false);
		shift.setRepeatWeekly((cursor.getInt(6) == 1) ? true : false);
		shift.setRepeatMonthly((cursor.getInt(7) == 1) ? true : false);
		shift.setWorked((cursor.getInt(8) == 1) ? true : false);
//		shift.setRepeatCount(cursor.getInt(9));
	//	shift.setRepeatCount(1);
		return shift;
	}

}
