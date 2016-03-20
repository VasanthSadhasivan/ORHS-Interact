package com.teacupofcode.dev.interactapp;

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

/**
 * Created by VasanthSadhasivan on 10/30/2015.
 */


public class MySpreadsheetIntegration extends AsyncTask<String, Void, String>{
    public static ArrayList<String> dateList;
    public static ArrayList<String> eventList;
    public static ArrayList<String> linkList=new ArrayList<String>();
    public final static String TAG = "MySpreadsheetI";

    public static String url ="https://spreadsheets.google.com/tq?key=1OYHhpeWnWCF_35foMF-R8oPha012UGDSBo1Q1aVUVJM";
    //public static String url ="https://spreadsheets.google.com/tq?key=1aoUbUIIQtmj2aubTwQF4XskgaESBlrMdzmli7IPzEcQ";
    public static ArrayList<String> getEventsSetLinks(String url)  throws IOException, JSONException
    {
        MySpreadsheetIntegration data = new MySpreadsheetIntegration();
        ArrayList<String> events = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();
        //String jsonData = data.downloadUrl("https://spreadsheets.google.com/tq?key=1MI6BIaeNRsti2VtaFJGKR3HdT1P0KLVJx7Au-WhvDS8");
        String jsonData = data.downloadUrl(url);


        JSONObject jsonObject = data.getJSONObject(jsonData);





        JSONObject table=jsonObject.getJSONObject("table");
        JSONArray rows = table.getJSONArray("rows");
        JSONObject arraydata_object = rows.getJSONObject(0);
        JSONObject linkdata_object  = rows.getJSONObject(1);
        Log.w(TAG, "arraydata_object"+ arraydata_object.toString());
        Log.w(TAG, "linkdata_object"+ arraydata_object.toString());
        JSONArray linkdata = linkdata_object.getJSONArray("c");
        JSONArray arraydata = arraydata_object.getJSONArray("c");

        Log.w(TAG, arraydata.length()+"");
        for(int i=0; i<arraydata.length(); i++) {
            try {
                if( arraydata.getJSONObject(i).getString("v") != null && arraydata.getJSONObject(i).getString("v") != "null" && arraydata.getJSONObject(i) != null && arraydata.getJSONObject(i).getString("v") != "") {
                    Log.w(TAG, arraydata.getJSONObject(i).getString("v") + " inside for loop");
                    events.add(arraydata.getJSONObject(i).getString("v"));
                }
                else
                {
                    continue;
                }
            }
            catch (JSONException e){
                continue;
            }

        }
        for(int i=0; i<linkdata.length(); i++) {
            try {
                if( linkdata.getJSONObject(i).getString("v") != null && linkdata.getJSONObject(i).getString("v") != "null" && linkdata.getJSONObject(i) != null && linkdata.getJSONObject(i).getString("v") != "") {
                    Log.w(TAG, linkdata.getJSONObject(i).getString("v") + " inside for loop");
                    links.add(linkdata.getJSONObject(i).getString("v"));
                }
                else
                {
                    continue;
                }
            }
            catch (JSONException e){
                continue;
            }

        }
        Log.w(TAG, events.get(0) +" events.get(0)");
        Log.w(TAG,""+events.size() +" events.get(0)");
        MySpreadsheetIntegration.linkList = links;
        return events;

    }

    public static ArrayList<String> getDate(String url) throws IOException, JSONException
    {
        ArrayList<String> eventsList = getEventsSetLinks(url);
        ArrayList<String> dateList = new ArrayList<String>();
        for(String a : eventsList)
        {

            String[] seperatedString = a.split("\\s+");
            Log.w(TAG, seperatedString[0]);
            dateList.add(seperatedString[seperatedString.length-1]);


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
        //"AAA AA BBB".split(" ")-->"AAA", "AA", "BBB"
        for(String a : listofdata)
        {
            a=MySpreadsheetIntegration.removeLastWord(a);
            /*tempString="";
            String[] listString = a.split(" ");
            ArrayList<String> seperatedString = new ArrayList<String>(Arrays.asList(listString));
            Log.w(TAG, seperatedString.get(0));
            for(String i : seperatedString)
            {
                if (!(i==seperatedString.get(seperatedString.size()-1)))

                    tempString += " "+i;
                else
                    break;
            }
            Log.w(TAG, "TEMPSTRING: " + tempString);
            returnString.add(tempString);
            Log.w(TAG, tempString);*/
            returnString.add(a);
        }
        Log.w(TAG,returnString.get(0));
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
            Log.w(TAG, "CONN>CONECT");
            conn.connect();
            Log.w(TAG, "CONN>DONE");
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
        int start = result.indexOf("{", 0);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);

        try {

            table = new JSONObject(jsonResponse+"}");
            Log.w(TAG, "JSONDATA " + jsonResponse + " AYY");
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
            MySpreadsheetIntegration.eventList=MySpreadsheetIntegration.getEventsSetLinks(MySpreadsheetIntegration.url);
        } catch (IOException e) {
            Log.w(TAG,"POOP");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.w(TAG,"POOP1");
            e.printStackTrace();
        }
        return "HI";

    }
    @Override
    protected void onPostExecute(String message) {

    }
}