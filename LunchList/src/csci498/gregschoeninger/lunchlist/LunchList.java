package csci498.gregschoeninger.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LunchList extends TabActivity {
	
	private List<Restaurant> restaurants = new ArrayList<Restaurant>();
	private ArrayAdapter<Restaurant> restaurantsAdapter;
	private RadioGroup types;
	private EditText name;
	private AutoCompleteTextView address;
	private EditText notes;
	private Restaurant current;
	private int progress;
	private AtomicBoolean isActive;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_lunch_list);
        
        types = (RadioGroup) findViewById(R.id.types);
        name = (EditText) findViewById(R.id.name); 
        address = (AutoCompleteTextView) findViewById(R.id.address);
        notes = (EditText) findViewById(R.id.notes); 
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        restaurantsAdapter = new RestaurantAdapter(); 
        list.setAdapter(restaurantsAdapter);
        list.setOnItemClickListener(onListClick);
        
        String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
        
        address = (AutoCompleteTextView)findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
        
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants); 
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        
        isActive = new AtomicBoolean(true);
        
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2"); 
        spec.setContent(R.id.details); 
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu); 
		return(super.onCreateOptionsMenu(menu));
    }
    
    
    private static class RestaurantHolder {
    	  protected TextView name;
    	  protected TextView address;
    	  protected ImageView icon;
    	  
    	  RestaurantHolder(View row) { 
    		  name = (TextView)row.findViewById(R.id.title); 
    		  address = (TextView)row.findViewById(R.id.address); 
    		  icon = (ImageView)row.findViewById(R.id.icon);
    	  }
    	  
    	  void populateBasicData(Restaurant r){
    		name.setText(r.getName());
      		address.setText(r.getAddress());
    	  }
    	  
    	  void populateFrom(Restaurant r){}
    }
    
    private static class TakeOutHolder extends RestaurantHolder {
    	TakeOutHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Restaurant r) { 
    		super.populateBasicData(r);
    		name.setTextColor(Color.DKGRAY);
    		icon.setImageResource(R.drawable.ball_yellow);
    	}
    }
    
    private static class SitDownHolder extends RestaurantHolder {
    	SitDownHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Restaurant r) { 
    		super.populateBasicData(r);
		  	name.setTextColor(Color.RED);
	  		icon.setImageResource(R.drawable.ball_red);
	  	}
    }
    
    private static class DeliveryHolder extends RestaurantHolder {
    	DeliveryHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Restaurant r) { 
    		super.populateBasicData(r);
	  		name.setTextColor(Color.GREEN);
	  		icon.setImageResource(R.drawable.ball_green);
	  	}
    }
    
    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    	
    	private static final int DELIVERY_TYPE = 0;
    	private static final int SIT_DOWN_TYPE = 1;
    	private static final int TAKEOUT_TYPE = 2;
    	
    	RestaurantAdapter() {
    		super(LunchList.this, android.R.layout.simple_list_item_1, restaurants);
    	} 
    	
    	public View getView(int position, View convertView, ViewGroup parent){
    		View row = convertView;
    		RestaurantHolder holder;
    		if (row == null) {
    			LayoutInflater inflater = getLayoutInflater();
    			row = inflater.inflate(R.layout.row, parent, false); 
    			int type = getItemViewType(position);
    			switch(type){
	    			case DELIVERY_TYPE:
	    				holder = new DeliveryHolder(row); 
	    				break;
	    			case SIT_DOWN_TYPE:
	    				holder = new SitDownHolder(row); 
	    				break;
	    			case TAKEOUT_TYPE:
	    				holder = new TakeOutHolder(row); 
	    				break;
	    			default:
	    				holder = new TakeOutHolder(row);
	    				break;
    			}
    			
    			row.setTag(holder);
    		} else {
    			holder = (RestaurantHolder)row.getTag(); 
    		}
    		
    		holder.populateFrom(restaurants.get(position)); 
    		return row;
    	}
    	
    	@Override
        public int getItemViewType(int position) {
    		Restaurant r = restaurants.get(position);
    		if(r.getType().equals("sit_down")){
    			return SIT_DOWN_TYPE;
    		} else if(r.getType().equals("delivery")){
    			return DELIVERY_TYPE;
    		} else {
    			return TAKEOUT_TYPE;
    		}
        }
 
        @Override
        public int getViewTypeCount() {
            return 3;
        }
    	
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
    	
    	public void onClick(View v) {
    		current = new Restaurant();
    		
    		current.setName(name.getText().toString());
    		current.setAddress(address.getText().toString()); 
	    	setRestaurantType(types, current);
	    	current.setNotes(notes.getText().toString()); 
	    	
	    	restaurantsAdapter.add(current);
	    	getTabHost().setCurrentTab(0);
	    }
    	
    	public void setRestaurantType(RadioGroup types, Restaurant r){
    		switch(types.getCheckedRadioButtonId()){
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
	    	
    	
    };
    
    private OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		current = restaurants.get(position);
    		name.setText(current.getName());
    		address.setText(current.getAddress());
    		notes.setText(current.getNotes());
    		
    		if(current.getType().equals("sit_down")){
    			types.check(R.id.sit_down);
    		} else if (current.getType().equals("take_out")){
    			types.check(R.id.take_out);
    		} else {
    			types.check(R.id.delivery);
    		}
    		getTabHost().setCurrentTab(1);
    	}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.toast) {
			String message = "No restaurant selected";
			if (current != null) { 
				message = current.getNotes();
			}
			Toast.makeText(this, message, Toast.LENGTH_LONG).show(); 
			return true;
		} else if(item.getItemId() == R.id.run){
			startWork();
			return true;
		}
		return super.onOptionsItemSelected(item); 
	}
	
	private void doSomeLongWork(final int incr){
		runOnUiThread(new Runnable(){
			public void run(){
				progress += incr;
				setProgress(progress);
			}
		});
    	SystemClock.sleep(250);
    }
    
    private Runnable longTask = new Runnable(){
    	public void run(){
    		for(int i = progress; i < 10000 && isActive.get(); i += 200)
    			doSomeLongWork(200);
    		
    		if(isActive.get()){
    			runOnUiThread(new Runnable(){
        			public void run(){
        				setProgressBarVisibility(false);
        				progress = 0;
        			}
        		});
    		}
    	}
    };
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
    	super.onSaveInstanceState(savedInstanceState);
    	savedInstanceState.putInt("progress", progress);
    	
    	
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	int savedProgress = savedInstanceState.getInt("progress");
    	progress = savedProgress;
      	isActive.set(true);
	  	if(progress > 0){
	  		startWork();
	  	}
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	isActive.set(false);
    }
    
    @Override 
    public void onResume(){
    	super.onResume();
    	
    	isActive.set(true);
    	if(progress > 0){
    		startWork();
    	}
    }
    
    private void startWork(){
    	setProgressBarVisibility(true);
    	new Thread(longTask).start();
    }
	
	
}
