package apt.tutorial;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LunchList extends ListActivity {
	
	private Cursor restaurants;
	private CursorAdapter restaurantsAdapter;
	private RestaurantHelper helper;
	public final static String ID_EXTRA = "apt.tutorial._ID";
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        helper = new RestaurantHelper(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        initList();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
    private void initList(){
    	if(restaurants != null){
    		stopManagingCursor(restaurants);
    		restaurants.close();
    	}
    	
    	restaurants = helper.getAll(prefs.getString("sort_order",  "name"));
    	startManagingCursor(restaurants);
    	restaurantsAdapter = new RestaurantAdapter(restaurants);
    	setListAdapter(restaurantsAdapter);
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
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
		Intent i = new Intent(LunchList.this, DetailForm.class);
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu); 
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.add){
			startActivity(new Intent(LunchList.this, DetailForm.class));
			return true;
		} else if(item.getItemId() == R.id.prefs){
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new OnSharedPreferenceChangeListener(){
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key){
			if(key.equals("sort_order")){
				initList();
			}
		}
	};
	
	

	@Override
	public void onDestroy(){
		super.onDestroy();
		helper.close();
	}
	
}
