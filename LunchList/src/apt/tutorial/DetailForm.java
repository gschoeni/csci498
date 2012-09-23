package apt.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	private RadioGroup types;
	private EditText name;
	private AutoCompleteTextView address;
	private EditText notes;
	private RestaurantHelper helper;
	private Restaurant current;
	private String addresses[] = { "Golden", "Boulder", "Denver", "Arvada", "Colorado"};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
        types = (RadioGroup) findViewById(R.id.types);
        name = (EditText) findViewById(R.id.name); 
        address = (AutoCompleteTextView) findViewById(R.id.address);
        notes = (EditText) findViewById(R.id.notes); 
        
        Button save = (Button)findViewById(R.id.save); 
        save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
    	
    	public void onClick(View v) {
    		current = new Restaurant();
    		
    		current.setName(name.getText().toString());
    		current.setAddress(address.getText().toString()); 
	    	setRestaurantType(types, current);
	    	current.setNotes(notes.getText().toString()); 
	    	
	    	helper.insert(name.getText().toString(), address.getText().toString(), current.getType(), notes.getText().toString());
	    	
	    	address = (AutoCompleteTextView)findViewById(R.id.address);
	        //address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses));
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
