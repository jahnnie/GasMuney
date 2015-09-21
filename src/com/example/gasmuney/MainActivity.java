package com.example.gasmuney;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "Bigga";
	TextView textView;
	private double price;
	ArrayList<Double> accumulated= new ArrayList<Double>();
	private int n;
	
	public Person alex = new Person("Alex", 0);
	public Person ebod = new Person("Ebod", 0);
	public Person joe = new Person("Joe", 0);
	
	public ArrayList<Person> homies = new ArrayList<Person>();
	int index = 0;
	
	public void setPrice(double price){
		this.price = price;		
	}
	public double getPrice(){
		return price;
	}
	
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences settings = getSharedPreferences("Homies", 0);
		alex.setAccumulated(Double.longBitsToDouble(settings.getLong("Alex", -69)));
		ebod.setAccumulated(Double.longBitsToDouble(settings.getLong("Ebod", -69)));
		joe.setAccumulated(Double.longBitsToDouble(settings.getLong("Joe", -69)));
		textView = (TextView)findViewById(R.id.textView1);
		Button yen = (Button) findViewById(R.id.button1);
		Button sho = (Button) findViewById(R.id.button2);
		Button stew = (Button) findViewById(R.id.button3);
		Button confirm = (Button) findViewById(R.id.button4);
		yen.setBackgroundColor(Color.parseColor("#DC5C05"));
		sho.setBackgroundColor(Color.parseColor("#FEAC00"));
		stew.setBackgroundColor(Color.parseColor("#6FC5B8"));
		new Price().execute();
		
/*		
		try{
	        Document document = Jsoup.connect(url).get();
	        Elements text = document.select("td");
	        price = Double.parseDouble(text.text().substring(6, 13));
	        }
			catch(IOException e){
				Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
			}
	*/

        yen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            	alex.setRiding(true);
            	homies.add(alex);
            	setN(getN()+1);
            }
        }); 
        
        yen.setOnTouchListener( new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch ( event.getAction() ) {
	            case MotionEvent.ACTION_DOWN: 
	            	v.setBackgroundColor(Color.parseColor("#00000000"));
	            	break;
	            case MotionEvent.ACTION_UP: 
	            	v.setBackgroundColor(Color.parseColor("#DC5C05"));
	            	break;
	            }
				return false;
			}
		} );
              
        sho.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ebod.setRiding(true);
            	homies.add(ebod);
            	setN(getN()+1);
            }
        }); 
        
        sho.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch ( event.getAction() ) {
	            case MotionEvent.ACTION_DOWN: 
	            	v.setBackgroundColor(Color.parseColor("#00000000"));
	            	break;
	            case MotionEvent.ACTION_UP: 
	            	v.setBackgroundColor(Color.parseColor("#FEAC00"));
	            	break;
	            }
				return false;
			}
		} );
        
        stew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	joe.setRiding(true);
            	homies.add(joe);
            	setN(getN()+1);
            }
        });
        stew.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch ( event.getAction() ) {
	            case MotionEvent.ACTION_DOWN: 
	            	v.setBackgroundColor(Color.parseColor("#00000000"));
	            	break;
	            case MotionEvent.ACTION_UP: 
	            	v.setBackgroundColor(Color.parseColor("#6FC5B8"));
	            	break;
	            }
				return false;
			}
		} );
       	/*
-	Display who owes how much, then hit confirm again if figures are satisfactory
o	After second confirm button press,
o	Add person.amountOwed to person.accumulated
*/
		
        confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
            	double rate = calculateRate();
            	for(Person p: homies){
            		if(p.isRiding())
            			p.setAmountOwed(rate);
            	}
            	
            	AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                alertDialog.setTitle("             Rate per trip");
                alertDialog.setMessage("Alex                   Ebod                   Joe" + "\n" + 
                String.valueOf(Math.round(alex.getAmountOwed() * 100.0) / 100.0) + "                   " + String.valueOf(Math.round(ebod.getAmountOwed() * 100.0) / 100.0) + "                   " + String.valueOf(Math.round(joe.getAmountOwed() * 100.0) / 100.0));

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            	for(Person p: homies){
                            		if(p.isRiding()){
                            			p.setAccumulated(p.getAccumulated() + p.getAmountOwed());
                            			accumulated.add(p.getAccumulated());
                            			p.setRiding(false);
                            			p.setAmountOwed(0);
                            		}
                            	}
                            }
                        });
                alertDialog.show();
            	setN(0);
            }
        });
	}
	
	public double calculateRate(){
			//set price here
			double rate = getPrice()/(getN() + 1);
			return rate;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.getItem(0).setTitle("Alex" + ": "+ String.valueOf(Math.round(alex.getAccumulated() * 100.0) / 100.0));
	    menu.getItem(1).setTitle("Ebod" + ": "+ String.valueOf(Math.round(ebod.getAccumulated() * 100.0) / 100.0));
	    menu.getItem(2).setTitle("Joe" + ": "+ String.valueOf(Math.round(joe.getAccumulated() * 100.0) / 100.0));
	    menu.getItem(3).setTitle("clear...");
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.nigga:
	    	final EditText input = new EditText(MainActivity.this);
	    	AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
	    	alertDialog.setView(input);
            alertDialog.setTitle("Pay Up!");
            alertDialog.setMessage("Who's paying?");
            //have user type in name, then enter
            //If name is X, clear accumulated
            alertDialog.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	 private class Price extends AsyncTask<String, Void, Void> {
		    Connection con = null;

		    @Override
		    protected Void doInBackground(String... params) {
		        Document doc = null;
		        if (con !=null){
		            try {
		                doc = con.get();
		                Elements element = doc.getElementsByClass("SideAvg");
		                setPrice(Double.parseDouble(element.text().substring(26, 33))/100);
		                }
		            catch (IOException except) {
		                except.printStackTrace();
		            }
		        }
		        else{
		        }
		        return null;
		    }
		    @Override
		    protected void onPreExecute() {
		        con = Jsoup.connect("http://www.vancouvergasprices.com").userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
		        }
		    @Override
		    protected void onPostExecute(Void result){
		        textView.setText("Avg PPL: " + String.valueOf(getPrice()));
		        textView.setTextColor(Color.RED);
		    }
		}
	 /*
	 public void onSaveInstanceState(Bundle savedState) {

		    super.onSaveInstanceState(savedState);

		    // Note: getValues() is a method in your ArrayAdaptor subclass
		    double[] values = new double[3];
		    for(int i = 0; i < 3; i++){
		    	values[i] = accumulated.get(i);
		    }
		    accumulated.clear();
		    //clear accumulated
		    savedState.putDoubleArray("myKey", values);

		}*/
	 @Override
	 public void onSaveInstanceState(Bundle savedInstanceState) {
	   super.onSaveInstanceState(savedInstanceState);
	   // Save state to the savedInstanceState
	   savedInstanceState.putDouble("Alex", Math.round(alex.getAccumulated() * 100.0) / 100.0);
	   savedInstanceState.putDouble("Ebod", Math.round(ebod.getAccumulated() * 100.0) / 100.0);
	   savedInstanceState.putDouble("Joe", Math.round(joe.getAccumulated() * 100.0) / 100.0);
	   //Log.v(TAG, "HELLO"); //works
	   
	 }
	 @Override
	 public void onRestoreInstanceState(Bundle savedInstanceState) {
	   super.onRestoreInstanceState(savedInstanceState);
	   // Restore state from savedInstanceState
	   alex.setAccumulated(savedInstanceState.getDouble("Alex"));
	   ebod.setAccumulated(savedInstanceState.getDouble("Ebod"));
	   joe.setAccumulated(savedInstanceState.getDouble("Joe"));  
	   //Log.v(TAG, String.valueOf(alex.getAccumulated()));
	 }
	 @Override
	 public void onStop(){
		 super.onStop();
		 SharedPreferences settings = getSharedPreferences("Homies", 0);
	     Editor editor = settings.edit();
	     editor.putLong("Alex", Double.doubleToLongBits(alex.getAccumulated())); //can't putDouble in editor so gotta convert
	     editor.putLong("Ebod", Double.doubleToLongBits(ebod.getAccumulated()));
	     editor.putLong("Joe", Double.doubleToLongBits(joe.getAccumulated()));
	      // Commit the edits!
	     editor.commit();
	 }
}
