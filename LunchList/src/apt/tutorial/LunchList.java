package apt.tutorial;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import apt.tutorial.DetailForm;
import apt.tutorial.R;

public class LunchList extends TabActivity {
	
	private Cursor restaurants;
	private CursorAdapter restaurantsAdapter;
	private RadioGroup types;
	private EditText name;
	private AutoCompleteTextView address;
	private EditText notes;
	private Restaurant current;
	private RestaurantHelper helper;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        helper = new RestaurantHelper(this);
        
        types = (RadioGroup) findViewById(R.id.types);
        name = (EditText) findViewById(R.id.name); 
        address = (AutoCompleteTextView) findViewById(R.id.address);
        notes = (EditText) findViewById(R.id.notes); 
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        restaurants = helper.getAll();
        startManagingCursor(restaurants);
        restaurantsAdapter = new RestaurantAdapter(restaurants);
        list.setAdapter(restaurantsAdapter);
        
        list.setOnItemClickListener(onListClick);
        
        String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
        
        address = (AutoCompleteTextView)findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
        
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants); 
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2"); 
        spec.setContent(R.id.details); 
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
        
        
        
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
    	  
    	  void populateBasicData(Cursor c, RestaurantHelper helper){
    		name.setText(helper.getName(c));
      		address.setText(helper.getAddress(c));
    	  }
    	  
    	  void populateFrom(Cursor c, RestaurantHelper helper){}
    }
    
    private static class TakeOutHolder extends RestaurantHolder {
    	TakeOutHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Cursor c, RestaurantHelper helper) { 
    		super.populateBasicData(c, helper);
    		name.setTextColor(Color.DKGRAY);
    		icon.setImageResource(R.drawable.ball_yellow);
    	}
    }
    
    private static class SitDownHolder extends RestaurantHolder {
    	SitDownHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Cursor c, RestaurantHelper helper) { 
    		super.populateBasicData(c, helper);
		  	name.setTextColor(Color.RED);
	  		icon.setImageResource(R.drawable.ball_red);
	  	}
    }
    
    private static class DeliveryHolder extends RestaurantHolder {
    	DeliveryHolder(View row){
    		super(row);
    	}
    	
    	void populateFrom(Cursor c, RestaurantHelper helper) { 
    		super.populateBasicData(c, helper);
	  		name.setTextColor(Color.GREEN);
	  		icon.setImageResource(R.drawable.ball_green);
	  	}
    }
    
    private class RestaurantAdapter extends CursorAdapter {
    	
    	private static final int DELIVERY_TYPE = 0;
    	private static final int SIT_DOWN_TYPE = 1;
    	private static final int TAKEOUT_TYPE = 2;
    	
    	RestaurantAdapter(Cursor c) {
    		super(LunchList.this, c);
    	} 
    	
    	@Override
    	public void bindView(View row, Context cxt, Cursor c){
    		RestaurantHolder holder = (RestaurantHolder)row.getTag();
    		holder.populateFrom(c, helper);
    	}
    	
    	@Override
    	public View newView(Context ctx, Cursor c, ViewGroup parent){
    		LayoutInflater inflater = getLayoutInflater();
    		View row = inflater.inflate(R.layout.row, parent, false);
    		RestaurantHolder holder = new RestaurantHolder(row);
    		row.setTag(holder);
    		return row;
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
    		
    		holder.populateFrom(restaurants, helper); 
    		return row;
    	}
    	
    	@Override
        public int getItemViewType(int position) {
    		restaurants.moveToPosition(position);
    		
    		if(helper.getType(restaurants).equals("sit_down")){
    			return SIT_DOWN_TYPE;
    		} else if(helper.getType(restaurants).equals("delivery")){
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
	    	
	    	helper.insert(name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString());
	    	restaurants.requery();
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
    
    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		Intent i = new Intent(LunchList.this, DetailForm.class);
    		startActivity(i);
    	}
	};
	

	@Override
	public void onDestroy(){
		super.onDestroy();
		helper.close();
	}
	
}
