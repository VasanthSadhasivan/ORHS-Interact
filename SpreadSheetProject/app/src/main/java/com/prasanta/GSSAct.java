/*
 * Copyright (C) 2011 Prasanta Paul, http://prasanta-paul.blogspot.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prasanta;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.prasanta.auth.AndroidAuthenticator;

/**
 * @author Prasanta Paul
 *
 */
public class GSSAct extends Activity {
    
	ArrayList<SpreadSheet> spreadSheets;
	TextView tv;
	ListView list;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list = new ListView(this.getApplicationContext());
        //setContentView(R.layout.main);
        //tv = (TextView)findViewById(R.id.sp_count);
        tv = new TextView(this.getApplicationContext());
        
        // Init and Read SpreadSheet list from Google Server
		new MyTask().execute((Object) null);
    }
	
	private class MyTask extends AsyncTask{

		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
            Log.w("AAYYY", "1");
			super.onPreExecute();
			dialog = new Dialog(GSSAct.this);
			dialog.setTitle("Please wait");
			TextView tv = new TextView(GSSAct.this.getApplicationContext());
			tv.setText("Featching SpreadSheet list from your account...");
			dialog.setContentView(tv);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
            Log.w("AAYYY", "2");

            // Read Spread Sheet list from the server.
			SpreadSheetFactory factory = SpreadSheetFactory.getInstance(new AndroidAuthenticator(GSSAct.this));
		    spreadSheets = factory.getSpreadSheet("Hours",true);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
            Log.w("AAYYY", "3");

            // TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog.isShowing())
				dialog.cancel();
			
			if(spreadSheets == null || spreadSheets.size() == 0){
		        tv.setText("No spreadsheet exists in your account...");
		        setContentView(tv);
		    }
		    else{
		        //tv.setText(spreadSheets.size() + "  spreadsheets exists in your account...");
		    	ArrayAdapter<String> arrayAdaper = new ArrayAdapter<String>(GSSAct.this.getApplicationContext(), android.R.layout.simple_list_item_1);
		    	for(int i=0; i<spreadSheets.size(); i++){
		    		SpreadSheet sp = spreadSheets.get(i);
		    		arrayAdaper.add(sp.getTitle());
		    	}
		    	list.addHeaderView(tv);
		    	list.setAdapter(arrayAdaper);
		    	tv.setText("Number of SpreadSheets ("+ spreadSheets.size() +")");
		    	
		    	list.setOnItemClickListener(new OnItemClickListener(){

					public void onItemClick(AdapterView<?> adapterView, View view,
							int position, long id) {
						// Show details of the SpreadSheet
						if(position == 0)
							return;
						
						Toast.makeText(GSSAct.this.getApplicationContext(), "Showing SP details, please wait...", Toast.LENGTH_LONG).show();
						
						// Start SP Details Activity 
						Intent i = new Intent(GSSAct.this, GSSDetails.class);
						i.putExtra("sp_id", position - 1);
						startActivity(i);
					}
		    	});
		    	setContentView(list);
		    }
		}

	}
}