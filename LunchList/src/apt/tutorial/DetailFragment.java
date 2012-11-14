package apt.tutorial;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
	
	private static final String ARG_REST_ID = "apt.tutorial.ARG_REST_ID";
	private RadioGroup types;
	private EditText name;
	private AutoCompleteTextView address;
	private EditText notes;
	private EditText feed;
	private TextView location;
	private RestaurantHelper helper;
	private Restaurant current;
	private String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
	String restaurantId;
	LocationManager locMan;
	private double latitude = 0.0d;
	private double longitude = 0.0d;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.detail_form, container, false);
    }
	
	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		initUI();
		Bundle args = getArguments();
		if (args != null) {
			loadRestaurant(args.getString(ARG_REST_ID));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
        restaurantId = getActivity().getIntent().getStringExtra(LunchFragment.ID_EXTRA);
        if (restaurantId != null) {
        	load();
        }
	}
	
	private RestaurantHelper getHelper() {
		if (helper == null) {
			helper = new RestaurantHelper(getActivity());
		}
		
		return helper;
	}
	
	public void loadRestaurant(String restaurantId) {
		this.restaurantId = restaurantId;
		
		if (restaurantId != null) {
			load();
		}
	}
	
	private void initUI() {
		types = (RadioGroup) getView().findViewById(R.id.types);
        name = (EditText) getView().findViewById(R.id.name); 
        address = (AutoCompleteTextView) getView().findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, addresses));
        notes = (EditText) getView().findViewById(R.id.notes);
        feed = (EditText) getView().findViewById(R.id.feed);
        location = (TextView) getView().findViewById(R.id.location);
	}
	
	private void load() {
		Cursor c = getHelper().getById(restaurantId);
		
		c.moveToFirst();
		name.setText(getHelper().getName(c));
		address.setText(getHelper().getAddress(c));
		notes.setText(getHelper().getNotes(c));
		feed.setText(getHelper().getFeed(c));
		
		if (getHelper().getType(c).equals("sit_down")) { 
			types.check(R.id.sit_down);
		} else if (getHelper().getType(c).equals("take_out")) {
			types.check(R.id.take_out); 
		} else { 
			types.check(R.id.delivery);
		}
		
		latitude = getHelper().getLatitude(c);
		longitude = getHelper().getLongitude(c);
		location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
		c.close();
	}
	
	private void save() {
		if (name.getText().toString().length() > 0) {
			current = new Restaurant();
			
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString()); 
	    	setRestaurantType(types, current);
	    	current.setNotes(notes.getText().toString()); 
	    	
	    	if (restaurantId == null) {
	    		getHelper().insert(name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString(), feed.getText().toString());
	    	} else {
	    		getHelper().update(restaurantId, name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString(), feed.getText().toString());
	    	}
		}
    }
	
	private void setRestaurantType(RadioGroup types, Restaurant r){
		switch (types.getCheckedRadioButtonId()) {
	    	case R.id.delivery:
	    		r.setType("delivery");
	    		break;
	    	case R.id.sit_down:
	    		r.setType("sit_down");
	    		break;
	    	case R.id.take_out:
	    		r.setType("take_out");
	    		break;
    	}
	}
    	
    @Override
	public void onSaveInstanceState(Bundle state){
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.details_option, menu);
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    	if (restaurantId == null) {
    		menu.findItem(R.id.location).setEnabled(false);
    		menu.findItem(R.id.map).setEnabled(false);
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.feed) {
    		if(isNetworkAvailable()){
    			Intent i = new Intent(getActivity(), FeedActivity.class);
        		i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
        		startActivity(i);
    		} else {
    			Toast.makeText(getActivity(), "Sorry, the Internet is not available", Toast.LENGTH_LONG).show();
    		}
    		
    		return true;
    	} else if (item.getItemId() == R.id.location) {
    		locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,  0, 0, onLocationChange);
    	} else if (item.getItemId() == R.id.map) {
    		Intent i = new Intent(getActivity(), RestaurantMap.class);
    		
    		i.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
    		i.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
    		i.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
    		
    		startActivity(i);
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    LocationListener onLocationChange = new LocationListener() {
    	public void onLocationChanged(Location fix) {
    		getHelper().updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
    		location.setText(String.valueOf(fix.getLatitude()) + ", " + fix.getLongitude());
    		locMan.removeUpdates(onLocationChange);
    		Toast.makeText(getActivity(), "Location saved", Toast.LENGTH_LONG).show();
    	}

		@Override
		public void onProviderDisabled(String arg0) { }

		@Override
		public void onProviderEnabled(String arg0) { }

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }
    };
    
    private boolean isNetworkAvailable(){
    	ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = cm.getActiveNetworkInfo();
    	return info != null;
    }
    
    public static DetailFragment newInstance(long id) {
    	DetailFragment result = new DetailFragment();
    	Bundle args = new Bundle();
    	
    	args.putString(ARG_REST_ID, String.valueOf(id));
    	result.setArguments(args);
    	
    	return result;
    }
    
    @Override
    public void onPause() {
    	save();
    	getHelper().close();
    	locMan.removeUpdates(onLocationChange);
    	super.onPause();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
	
}
