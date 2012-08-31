package csci498.gregschoeninger.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LunchList extends Activity {
	
	Restaurant r=new Restaurant();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
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
	    	EditText name = (EditText)findViewById(R.id.name); 
	    	EditText address = (EditText)findViewById(R.id.addr);
	    	r.setName(name.getText().toString());
	    	r.setAddress(address.getText().toString()); 
    	}
    	
    };
}
