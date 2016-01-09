package com.teacupofcode.dev.interactapp;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;
/**
 * Created by Vasanth Sadhasivan on 10/30/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
public class MySpreadsheetIntegration extends AsyncTask<String, Void, String>{
    public static ArrayList<String> dateList;
    public static ArrayList<String> eventList;
    public static ArrayList<String> linkList=new ArrayList<String>();
    public static String url ="https://spreadsheets.google.com/tq?key=1aoUbUIIQtmj2aubTwQF4XskgaESBlrMdzmli7IPzEcQ";
    public static ArrayList<String> getEvents(String url)  throws IOException, JSONException
    {
        MySpreadsheetIntegration data = new MySpreadsheetIntegration();
        ArrayList<String> events = new ArrayList<String>();
        //String jsonData = data.downloadUrl("https://spreadsheets.google.com/tq?key=1MI6BIaeNRsti2VtaFJGKR3HdT1P0KLVJx7Au-WhvDS8");
        Log.w("AYY", "getEvents:1"+url);
        String jsonData = data.downloadUrl(url);
        Log.w("AYY", "getEvents:2"+url);
        JSONObject jsonObject = data.getJSONObject(jsonData);
        Log.w("AYY", "getEvents:3"+url);
        JSONArray arraydata=jsonObject.getJSONArray("cols");
        Log.w("AYY", "getEvents:4");
        for(int i=0; i<arraydata.length(); i++)
        {
            if (!(arraydata.getJSONObject(i).getString("label").contains("Name") || arraydata.getJSONObject(i).getString("label").contains("TOTAL") || arraydata.getJSONObject(i).getString("label").equals("")))
            {
                if(!((arraydata.getJSONObject(i).getString("label")).contains("NAME")))
                    events.add(arraydata.getJSONObject(i).getString("label"));
            }

        }
        //ArrayList<String> returnEvents = MySpreadsheetIntegration.filterEvents(events);
        //Log.w("AYY", "FINISHED RETURNING:"+returnEvents.get(0));
        Log.w("Dustin is a fagboi", events.get(0));
        return events;

    }
    public static void generateLinks(){
        Log.w("DUSTIN IS A FAGBOI", MySpreadsheetIntegration.eventList.get(0));
        for (String i : MySpreadsheetIntegration.eventList)
        {
            Log.w("DUSTIN IS A FAGBOI", ""+(i.split("\\s+")).length);
            Log.w("DUSTIN IS A FAGBOI", (i.split("\\s+"))[(i.split("\\s+")).length-1]);

            MySpreadsheetIntegration.linkList.add((i.split("\\s+"))[(i.split("\\s+")).length - 1]);
        }
    }
    public static ArrayList<String> getDate(String url) throws IOException, JSONException
    {
        Log.w("AYY", "getDate:1" );
        ArrayList<String> eventsList = getEvents(url);
        Log.w("AYY", "getDate:"+eventsList.get(0));
        ArrayList<String> dateList = new ArrayList<String>();
        for(String a : eventsList)
        {

            String[] seperatedString = a.split("\\s+");
            Log.w("AYY", seperatedString[0]);
            dateList.add(seperatedString[seperatedString.length-2]);


        }


        return dateList;
    }
    public static String removeLastWord(String a)
    {
        String returnString="";
        String[] temp = a.split("\\s+");
        for(int i=0; i < temp.length-1; i++)
        {
            returnString += temp[i]+" ";
        }
        return returnString;
    }
    public static ArrayList<String> filterEvents(ArrayList<String> listofdata)
    {
        //listofdata is made up of title + date + link
        String tempString="";
        ArrayList<String> returnString = new ArrayList<String>();
        MySpreadsheetIntegration.generateLinks();
        //"AAA AA BBB".split(" ")-->"AAA", "AA", "BBB"
        for(String a : listofdata)
        {
            a=MySpreadsheetIntegration.removeLastWord(a);
            tempString="";
            String[] listString = a.split(" ");
            ArrayList<String> seperatedString = new ArrayList<String>(Arrays.asList(listString));
            Log.w("AYY", seperatedString.get(0));
            for(String i : seperatedString)
            {
                if (!(i==seperatedString.get(seperatedString.size()-1)))

                    tempString += " "+i;
                else
                    break;
            }
            Log.w("AYY", "TEMPSTRING: "+tempString);
            returnString.add(tempString);
            Log.w("Fucking stupid", tempString);
        }
        Log.w("AYYY",returnString.get(0));
        return returnString;
    }
    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            Log.w("AYY","CONN>CONECT");
            conn.connect();
            Log.w("AYY", "CONN>DONE");
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected JSONObject getJSONObject(String result) {
        // remove the unnecessary parts from the response and construct a JSON
        JSONObject table=null;
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);
        try {
            table = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return table;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String[] a){

        try {
            MySpreadsheetIntegration.dateList=MySpreadsheetIntegration.getDate(MySpreadsheetIntegration.url);
            MySpreadsheetIntegration.eventList=MySpreadsheetIntegration.getEvents(MySpreadsheetIntegration.url);
        } catch (IOException e) {
            Log.w("AYYY","POOP");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.w("AYYY","POOP1");
            e.printStackTrace();
        }
        return "HI";

    }
    @Override
    protected void onPostExecute(String message) {

    }
}