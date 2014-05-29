package com.example.workhours.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workhours.R;
import com.example.workhours.entities.Shift;

public class EventAdapter extends ArrayAdapter<Shift> {
	private Context context;
	private List<Shift> list;
	private int layoutId;
	private boolean showImage;

	public EventAdapter(Context context, int layoutId, List<Shift> list, boolean showImage) {		
		super(context, layoutId, list);
		this.layoutId = layoutId;
		this.context = context;
		this.list = list;
		this.showImage = showImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(layoutId, parent, false);
    	}
        Shift s = list.get(position);
		// get the view and fill with data and id
        try{
	        TextView from = (TextView) convertView.findViewById(R.id.tvFrom);
	        from.setText(s.getFromFormatted());
	        from.setTag(s.getId());
	        
	        TextView to = (TextView) convertView.findViewById(R.id.tvTo);
	        to.setText(s.getToFormatted());
	        
	        TextView hours = (TextView) convertView.findViewById(R.id.tvHours);
	        hours.setText(Integer.toString(s.getHours()) + " ID: "  +Integer.toString(s.getId()));
	        
	        if(showImage){
		        ImageView conf = (ImageView) convertView.findViewById(R.id.tvConfirmed);
		        if(s.isWorked())
		        	conf.setImageResource(R.drawable.status_ok);
		        else
		        	conf.setImageResource(R.drawable.status_notnok);
	        }
	        
        }catch(Exception e){
        	Log.d("Noe feiler", "");
        }
        return convertView;

	  }
	
	public void swapItems(List<Shift> items){
		list.clear();
		this.list = items;
		notifyDataSetChanged();
	}
}
