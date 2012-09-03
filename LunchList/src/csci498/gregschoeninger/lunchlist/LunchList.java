package csci498.gregschoeninger.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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

public class LunchList extends TabActivity {
	
	private List<Restaurant> restaurants = new ArrayList<Restaurant>();
	private ArrayAdapter<Restaurant> restaurantsAdapter = null;
	private RadioGroup types;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        types = (RadioGroup) findViewById(R.id.types);
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        restaurantsAdapter = new RestaurantAdapter(); 
        list.setAdapter(restaurantsAdapter);
        list.setOnItemClickListener(onListClick);
        
        String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
        
        AutoCompleteTextView address = (AutoCompleteTextView)findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
        
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants); spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        
        getTabHost().addTab(spec);
        spec = getTabHost().newTabSpec("tag2"); 
        spec.setContent(R.id.details); spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
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
    		Restaurant restaurant = new Restaurant();
    		
	    	EditText name = (EditText)findViewById(R.id.name); 
	    	AutoCompleteTextView address = (AutoCompleteTextView)findViewById(R.id.address);
	    	
	    	restaurant.setName(name.getText().toString());
	    	restaurant.setAddress(address.getText().toString()); 
	    	setRestaurantType(types, restaurant);
	    	
	    	restaurantsAdapter.add(restaurant);
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
    		
    	}
	};
}
