package csci498.gregschoeninger.lunchlist;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LunchList extends Activity {
	
	private Restaurant restaurant = new Restaurant();
	private RadioGroup types;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        addTypesRadioGroup();
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
    private void addTypesRadioGroup(){
    	types = (RadioGroup)findViewById(R.id.types);
        
        RadioButton sit_down = new RadioButton(this);
        sit_down.setText(R.string.sit_down);
        sit_down.setId(R.id.sit_down);
        types.addView(sit_down);
        
        RadioButton takeout = new RadioButton(this);
        takeout.setText(R.string.take_out);
        takeout.setId(R.id.take_out);
        types.addView(takeout);
        
        RadioButton delivery = new RadioButton(this);
        delivery.setText(R.string.delivery);
        delivery.setId(R.id.delivery);
        types.addView(delivery);
        
        RadioButton buffet = new RadioButton(this);
        buffet.setText(R.string.buffet);
        buffet.setId(R.id.buffet);
        types.addView(buffet);
        
        RadioButton fast_food = new RadioButton(this);
        fast_food.setText(R.string.fast_food);
        fast_food.setId(R.id.fast_food);
        types.addView(fast_food);
        
        types.check(delivery.getId());
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
    	
    	public void onClick(View v) {
	    	EditText name = (EditText)findViewById(R.id.name); 
	    	EditText address = (EditText)findViewById(R.id.addr);
	    	restaurant.setName(name.getText().toString());
	    	restaurant.setAddress(address.getText().toString()); 
	    	
	    	setRestaurantType(types);
	    }
    	
    	public void setRestaurantType(RadioGroup types){
    		switch(types.getCheckedRadioButtonId()){
		    	case R.id.sit_down:
		    		restaurant.setType("sit_down");
		    		break;
		    	case R.id.take_out:
		    		restaurant.setType("take_out");
		    		break;
		    	case R.id.delivery:
		    		restaurant.setType("delivery");
		    		break;
	    	}
    		
    	}
	    	
    	
    };
}
