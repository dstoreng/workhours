package com.example.workhours.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workhours.R;

public class ProfileFragmentDetails extends Fragment {
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	      //  return inflater.inflate(R.layout.fragment_profile_details, container, false);
		 return inflater.inflate(R.layout.test, container, false);
	    }

}
