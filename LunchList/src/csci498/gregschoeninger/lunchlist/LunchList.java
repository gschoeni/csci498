package csci498.gregschoeninger.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LunchList extends Activity {
	
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
        
        String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
        
        AutoCompleteTextView address = (AutoCompleteTextView)findViewById(R.id.address);
        address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
    static class RestaurantHolder {
    	  private TextView name;
    	  private TextView address;
    	  private ImageView icon;
    	  
    	  RestaurantHolder(View row) { 
    		  name = (TextView)row.findViewById(R.id.title); 
    		  address = (TextView)row.findViewById(R.id.address); 
    		  icon = (ImageView)row.findViewById(R.id.icon);
    	  }
    	  
    	  void populateFrom(Restaurant r) { 
    		  name.setText(r.getName()); 
    		  address.setText(r.getAddress());
    		  
    		  if (r.getType().equals("sit_down")) {
    			  icon.setImageResource(R.drawable.ball_red); 
    		  } else if (r.getType().equals("take_out")) { 
    			  icon.setImageResource(R.drawable.ball_yellow);
    		  } else {
    			  icon.setImageResource(R.drawable.ball_green); 
    		  }
    	  }
    	  
    }
    
    class RestaurantAdapter extends ArrayAdapter<Restaurant> { 
    	RestaurantAdapter() {
    		super(LunchList.this, android.R.layout.simple_list_item_1, restaurants);
    	} 
    	
    	public View getView(int position, View convertView, ViewGroup parent){
    		View row = convertView;
    		RestaurantHolder holder;
    		if (row == null) {
    			LayoutInflater inflater = getLayoutInflater();
    			row = inflater.inflate(R.layout.row, parent, false); 
    			holder = new RestaurantHolder(row); 
    			row.setTag(holder);
    		} else {
    			holder = (RestaurantHolder)row.getTag(); 
    		}
    		
    		holder.populateFrom(restaurants.get(position)); 
    		return row;
    	}
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
    	
    	public void onClick(View v) {
    		Restaurant restaurant = new Restaurant();
    		
	    	EditText name = (EditText)findViewById(R.id.name); 
	    	EditText address = (EditText)findViewById(R.id.address);
	    	
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
}
