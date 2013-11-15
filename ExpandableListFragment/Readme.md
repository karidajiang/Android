ExpandableList with Checkbox in Android
------------

We are going to create a fragment for ExpandableList, in which checkboxes are 
enabled on both the parent/group view and the child view. This is achieved by
using the customized ExpandableList Adaptor.

The fragment here does not play an important role. However, ExpandableList 
usually resides in the view together with some other UI elements. In this case,
it is easier to use fragment for better module management.

This code is built on top of the code by [Ravi Tamada][2]

### Classes

The code is based on the following structure:

* **MainActivity.java**

> Start of the app. It initialize the *ExpListFragment* and keep a reference to it. In 
addition, it handles the call back from events of *ExpListFragment*. 

> Please note that the callback is **implicitly** handled without
 using [the interface mechanism][1] by Andriod. 


* **ExpListFragment.java**

> The Fragment for handling all events for the expandable list, 
including supplying it with data sources, and contains
a reference to the *ExpandableListAdapter*. 
It also corresponds to the *explist* xml layout file below.

* **explist.xml**

> The layout file in /res/layout which contains an *ExpandableListView* instance

* **ExpandableListAdapter.java**

> It extends *BaseExpandableListAdapter* and is responsible for most of the events triggered
by user interaction with the UI.

* **listgroup.xml** and **listitem.xml**

> These are the two xml files for inflating **one** group or item.


### Implementation
	
* **listgroup.xml** and **listitem.xml** in res/layout/
	
	* listgroup.xml
	~~~
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:orientation="horizontal" >
    
	    <TextView
	        android:id="@+id/lblListHeader"
	        android:layout_width="0dip"
	        android:layout_height="wrap_content"
	        android:textSize="17sp"
	        android:gravity="center_horizontal"
	        android:layout_weight="1"
	        android:paddingBottom="5dp"
	        android:paddingLeft="?android:attr/expandableListPreferredChildPaddingLeft" />       
	
	    <CheckBox
	        android:id="@+id/lblListParentCheck"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:gravity="right"
	        android:focusable="false" />
	</LinearLayout>
	~~~
	
	* listitem.xml
	~~~
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:orientation="horizontal" >
	        
	    <TextView
	        android:id="@+id/lblListItem"
	        android:layout_width="0dip"
	        android:layout_height="wrap_content"
	        android:textSize="17sp"
	        android:gravity="center_horizontal"
	        android:layout_weight="1"
	        android:paddingBottom="5dp"
	        android:paddingLeft="?android:attr/expandableListPreferredChildPaddingLeft" />
	 
	    <CheckBox
	        android:id="@+id/lblListChildCheck"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:gravity="right" 
	        android:focusable="false"/>
	</LinearLayout>
	~~~
	
	
	
* **ExpandableListAdapter.java**

	* Extends BaseExpandableListAdapter

	* Member Variables
	~~~c++
	private Context _context;
	// Store Data
    	private List<String> _listDataHeader; // header titles
    	// child data in format of key: header string, value: list of child string
    	private HashMap<String, List<String>> _listDataChild;
    	//store the boolean checked values
    	private List<Boolean> _checkListParent;
    	private HashMap<String, List<Boolean>> _checkListChild;
	~~~

	* Initialization
	~~~c++
	public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
	        this._context = context;
	        this._listDataHeader = listDataHeader;
	        this._listDataChild = listChildData;
	        this._checkListParent = new ArrayList<Boolean>();
	        this._checkListChild = new HashMap<String, List<Boolean>>();
	        this._checkListParent = new ArrayList<Boolean>();
	        initCheckList(listDataHeader, listChildData);
    	}
	~~~
	
	* Manage Checkbox Boolean Variables
	~~~c++
	private void initCheckList(List<String> listDataHeader,
            					HashMap<String, List<String>> listChildData);
	public void cleanCheckList();
	~~~

	* Overriding funcitons
	~~~c++
	// This function decides how each child view will display.
	// It know the current Group and/or Child position
	// We set up the listener for checkboxes here
	@Override
	public View getChildView(int groupPosition, final int childPosition,
	    boolean isLastChild, View convertView, ViewGroup parent);
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	    View convertView, ViewGroup parent);
	~~~


	* Helper function to change the boolean data
	~~~c++
	public void setParentCheckBox(final int groupPosition);
	public void setChildCheckBox(final String p_str, final int parentPosition, final int childPosition);
	~~~
	
	
	
* **explist.xml** in res/layout/

	* Create an *ExpandableListView* object in the UI with id *lvExp* 

	~~~
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical" >
	    <ExpandableListView
			android:id="@+id/lvExp"
			android:layout_height="wrap_content"
			android:layout_width="match_parent"/> 
	</LinearLayout>
	~~~
	
	
* **ExpListFragment.java**

	* Member Variable
	~~~c++
	// reference to the ExpandableListAdapter
	ExpandableListAdapter listAdapter;
	// reference to the UI object 
    	ExpandableListView expListView;
	// The header data in a list
	List<String> listDataHeader;
	// The child data. The key is the parent/group name, and value is a list of child 
	HashMap<String, List<String>> listDataChild;
	~~~
	
	* Initialize Data and *ExpandableListAdapter*
	~~~c++
	public View onCreateView(LayoutInflater inflater, 
    		ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.explist, 
		                         container, false);
		// ---- get the listview
		expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
		// preparing list data
		prepareListData();
		// create the ExpandableListAdapter
		listAdapter = new ExpandableListAdapter(inflater.getContext(), listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);
		return view;
	}
	~~~
	
	* prepareListData() funciton 
	~~~c++
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
	~~~	
	
	
	
* **activity_main.xml** in res/layout/

	* Change it to a FrameLayout with id *fragment_container*

	~~~
	<?xml version="1.0" encoding="utf-8"?>
	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/fragment_container"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent" />
	~~~

* **MainActivity.java**

	* Extends FragmentActivity

	* Member Vairable:
	~~~c++
	// reference to the fragment
	ExpListFragment labelfrag;
	~~~

	* Initialize the reference to *ExpListFragment* 
	~~~
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Check that the activity is using the FrameLayout with
		// id: fragment_container 
		if (findViewById(R.id.fragment_container) != null) {
	    		// Create a new Fragment to be placed in the activity layout
			explistfrag = new ExpListFragment();
		        // In case this activity was started with special instructions from an
		        // Intent, pass the Intent's extras to the fragment as arguments
			explistfrag.setArguments(getIntent().getExtras());
		        // Add the fragment to the 'fragment_container' FrameLayout
		        getSupportFragmentManager().beginTransaction()
		                .add(R.id.fragment_container, explistfrag).commit();      
		}   
	}
	~~~
	
	
	
	
	


[1]: http://developer.android.com/guide/components/fragments.html#CommunicatingWithActivity
[2]: http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/

