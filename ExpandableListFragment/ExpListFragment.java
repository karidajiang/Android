package com.example.expandablelistcheckbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class ExpListFragment extends Fragment {
	
	// reference to the ExpandableListAdapter
	ExpandableListAdapter listAdapter;
	// reference to the UI object 
    ExpandableListView expListView;
    
    // The header data in a list
    List<String> listDataHeader;
    // The child data. The key is the parent/group name, and value is a list of child 
    HashMap<String, List<String>> listDataChild;
    
    //
	
	@Override
    public View onCreateView(LayoutInflater inflater, 
    		ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.explist, 
                                 container, false);
       
        // ---- get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(inflater.getContext(), listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        return view;
        

	}
	
	/*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
 
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
 
        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
 
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
 
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
