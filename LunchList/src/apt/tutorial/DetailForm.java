package apt.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
		LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        initUI();
        
        restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
        if (restaurantId != null) {
        	load();
        }
	}
	
	private void initUI() {
		types = (RadioGroup) findViewById(R.id.types);
        name = (EditText) findViewById(R.id.name); 
        address = (AutoCompleteTextView) findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
        notes = (EditText) findViewById(R.id.notes);
        feed = (EditText) findViewById(R.id.feed);
        location = (TextView) findViewById(R.id.location);
	}
	
	private void load() {
		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
		if (helper.getType(c).equals("sit_down")) { 
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out); 
		} else { 
			types.check(R.id.delivery);
		}
		location.setText(String.valueOf(helper.getLatitude(c)) + ", " + String.valueOf(helper.getLongitude(c)));
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
	    		helper.insert(name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString(), feed.getText().toString());
	    	} else {
	    		helper.update(restaurantId, name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString(), feed.getText().toString());
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
    public void onRestoreInstanceState(Bundle state){
    	super.onRestoreInstanceState(state);
    	name.setText(state.getString("name"));
    	address.setText(state.getString("adderss"));
    	notes.setText(state.getString("notes"));
    	types.check(state.getInt("type"));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.details_option, menu); 
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.feed) {
    		if(isNetworkAvailable()){
    			Intent i = new Intent(this, FeedActivity.class);
        		i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
        		startActivity(i);
    		} else {
    			Toast.makeText(this, "Sorry, the Internet is not available", Toast.LENGTH_LONG).show();
    		}
    		
    		return true;
    	} else if (item.getItemId() == R.id.location) {
    		locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,  0, 0, onLocationChange);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    LocationListener onLocationChange = new LocationListener() {
    	public void onLocationChanged(Location fix) {
    		helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
    		location.setText(String.valueOf(fix.getLatitude()) + ", " + fix.getLongitude());
    		locMan.removeUpdates(onLocationChange);
    		Toast.makeText(DetailForm.this, "Location saved", Toast.LENGTH_LONG).show();
    	}

		@Override
		public void onProviderDisabled(String arg0) { }

		@Override
		public void onProviderEnabled(String arg0) { }

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }
    };
    
    private boolean isNetworkAvailable(){
    	ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
    	NetworkInfo info = cm.getActiveNetworkInfo();
    	return info != null;
    }
    
    @Override
    public void onPause() {
    	save();
    	locMan.removeUpdates(onLocationChange);
    	super.onPause();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	helper.close();
    }
	
}
