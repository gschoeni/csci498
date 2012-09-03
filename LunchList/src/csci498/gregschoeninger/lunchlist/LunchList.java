package csci498.gregschoeninger.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
        
        Spinner list = (Spinner)findViewById(R.id.restaurants);
        restaurantsAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, restaurants); 
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

    class RestaurantAdapter extends ArrayAdapter<Restaurant> { RestaurantAdapter() {
        super(LunchList.this,
              android.R.layout.simple_list_item_1,
              restaurants);
    } }

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
