package apt.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LunchFragment extends ListFragment {
	
	private Cursor restaurants;
	private CursorAdapter restaurantsAdapter;
	private RestaurantHelper helper;
	public final static String ID_EXTRA = "apt.tutorial._ID";
	private SharedPreferences prefs;
	private OnRestaurantListener listener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	helper = new RestaurantHelper(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initList();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
    private void initList(){
    	if(restaurants != null){
    		restaurants.close();
    	}
    	
    	restaurants = helper.getAll(prefs.getString("sort_order",  "name"));
    	restaurantsAdapter = new RestaurantAdapter(restaurants);
    	setListAdapter(restaurantsAdapter);
    }
    
    public interface OnRestaurantListener { 
    	void onRestaurantSelected(long id);
    }
    
    public void setOnRestaurantListener(OnRestaurantListener listener) { 
    	this.listener=listener;
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
    	if (listener != null) { 
    		listener.onRestaurantSelected(id);
    	}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.option, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.add){
			startActivity(new Intent(getActivity(), DetailForm.class));
			return true;
		} else if(item.getItemId() == R.id.prefs){
			startActivity(new Intent(getActivity(), EditPreferences.class));
			return true;
		} else if (item.getItemId() == R.id.help) {
    		startActivity(new Intent(getActivity(), HelpPage.class));
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
	public void onPause() {
		helper.close();
		super.onPause();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
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
	   		super(getActivity(), c);
	   	} 
	   	
	   	@Override
	   	public void bindView(View row, Context cxt, Cursor c){
	   		RestaurantHolder holder = (RestaurantHolder)row.getTag();
	   		holder.populateFrom(c, helper);
	   	}
	   	
	   	@Override
	   	public View newView(Context ctx, Cursor c, ViewGroup parent){
	   		LayoutInflater inflater = getActivity().getLayoutInflater();
	   		View row = inflater.inflate(R.layout.row, parent, false);
	   		RestaurantHolder holder = new RestaurantHolder(row);
	   		row.setTag(holder);
	   		return row;
	   	}
	   	
	   	public View getView(int position, View convertView, ViewGroup parent){
	   		View row = convertView;
	   		RestaurantHolder holder;
	   		if (row == null) {
	   			LayoutInflater inflater = getActivity().getLayoutInflater();
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
	   		Log.d("fkjlsa", "Type is: "+ helper.getType(restaurants));
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
	
}
