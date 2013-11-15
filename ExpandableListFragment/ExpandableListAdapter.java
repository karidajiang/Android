package com.example.expandablelistcheckbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
	private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    
    //store the checked values
    private List<Boolean> _checkListParent;
    private HashMap<String, List<Boolean>> _checkListChild;
    
    //private CheckBox checkbox;
    private void initCheckList(List<String> listDataHeader,
            					HashMap<String, List<String>> listChildData){
    	
    	for(int i=0; i<listDataHeader.size(); i++){
        	this._checkListParent.add(false);
        	//initialize all child checkbox values
        	int len = listChildData.get(listDataHeader.get(i)).size();
        	List<Boolean> checklist = new ArrayList<Boolean>();
        	for(int j=0; j<len; j++){
        		checklist.add(false);
        	}
        	this._checkListChild.put(listDataHeader.get(i), checklist);
        }
    }
    
    public void cleanCheckList(){
    	// clean up all the booleans
    	String groupname;
		for(int i=0; i<this._checkListParent.size(); i++){
			this._checkListParent.set(i, false);
			//initialize all child checkbox values
			groupname = this._listDataHeader.get(i);
			int len = this._listDataChild.get(groupname).size();
			for(int j=0; j<len; j++){
				this._checkListChild.get(groupname).set(j, false);
			}
		}
    }
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        
        
        this._checkListParent = new ArrayList<Boolean>();
        this._checkListChild = new HashMap<String, List<Boolean>>();
        
        //to debug
        Log.i("ExpandableAdapter", "helloworld");
        
        
        this._checkListParent = new ArrayList<Boolean>();
        initCheckList(listDataHeader, listChildData);
   
    }
    
    
    /* ----- overriding funtions ----- */
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	
    	//Log.i("getChildView", "called");
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        
        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.lblListChildCheck);
        final String p_str = this._listDataHeader.get(groupPosition);
        final int child_pos = childPosition;
        final int parent_pos = groupPosition;
        checkbox.setChecked(this._checkListChild.get(p_str).get(child_pos));
        
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean value) {
            	
            	if (checkbox.isPressed()) { // this is because getChildView is called multiple times on a single click
            		setChildCheckBox(p_str, parent_pos, child_pos);
            		//Log.i("checkonclick", ""+p_str+":"+child_pos);
            	}
            }
        });
        
        txtListChild.setText(childText);
        return convertView;
    }
    
    public void setParentCheckBox(final int groupPosition){
    	//Log.i("setparenttag", ""+this._checkListParent.get(groupPosition));
    	Boolean b = this._checkListParent.get(groupPosition);
    	b = !b;
    	this._checkListParent.set(groupPosition, b);
    	//Log.i("setparenttag", ""+this._checkListParent.get(groupPosition));
    }
    
    public void setChildCheckBox(final String p_str, final int parentPosition, final int childPosition){
    	//Log.i("setchildtag", p_str+":"+this._checkListChild.get(p_str).get(childPosition));
    	Boolean b = this._checkListChild.get(p_str).get(childPosition);
    	b = !b;
    	this._checkListChild.get(p_str).set(childPosition, b);
    	//Log.i("setchildtag", p_str+":"+this._checkListChild.get(p_str).get(childPosition));
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroup, null);
        }
        
        // get the pointer to the text view and the check box
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        
        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.lblListParentCheck);
        checkbox.setChecked(this._checkListParent.get(groupPosition));
        
        final int pos = groupPosition;
        
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean value) {
            	//_checkListParent.set(groupPosition, !_checkListParent.get(groupPosition));
            	if (checkbox.isPressed()) {
            		setParentCheckBox(pos);
            		//Log.i("checkonclick", ""+pos);
            	}
            }
        });
        
        //Log.i("getGroupView", " is clicked");
        
 
        return convertView;
    }
    
    
}
