package csci498.gregschoeninger.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class LunchList extends Activity {
	
	private List<Restaurant> restaurants = new ArrayList<Restaurant>();
	private RadioGroup types;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        types = (RadioGroup) findViewById(R.id.types);
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }

    
    private View.OnClickListener onSave = new View.OnClickListener() {
    	
    	public void onClick(View v) {
    		Restaurant restaurant = new Restaurant();
    		
	    	EditText name = (EditText)findViewById(R.id.name); 
	    	EditText address = (EditText)findViewById(R.id.addr);
	    	restaurant.setName(name.getText().toString());
	    	restaurant.setAddress(address.getText().toString()); 
	    	
	    	setRestaurantType(types);
	    }
    	
    	public void setRestaurantType(RadioGroup types){
    		switch(types.getCheckedRadioButtonId()){
		    	case R.id.delivery:
		    		//restaurants.get(0).setType("delivery");
		    		break;
		    	case R.id.sit_down:
		    		//restaurants.get(0).setType("sit_down");
		    		break;
		    	case R.id.take_out:
		    		//restaurants.get(0).setType("take_out");
		    		break;
	    	}
    		
    	}
	    	
    	
    };
}
