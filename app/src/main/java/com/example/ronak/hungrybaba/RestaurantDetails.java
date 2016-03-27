package com.example.ronak.hungrybaba;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantDetails extends AppCompatActivity {
private  restaurant rest;
    private ProgressBar spinner;
   private String[] food;
    Resources res;
    private String[] types;
    private int hid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        //String name=getIntent().getStringExtra("hotelName");
        rest=(restaurant)getIntent().getSerializableExtra("hotelObject");
        hid=getIntent().getIntExtra("hid",1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        ImageView hotelImage=(ImageView)findViewById(R.id.HotelImage);
        res = getResources();
        int resID = res.getIdentifier(rest.getIcon_id() , "drawable", getPackageName());
        hotelImage.setImageResource(resID);
        TextView text=(TextView)findViewById(R.id.Nametext);
        text.setText(rest.getName());
        Log.d("hid","hid="+hid);
        String furl="http://www.hungrybaba.esy.es/foodfetch.php?hid="+hid;
        new foodfetch().execute(furl);
        //populateList();
        //onListViewClick();

    }
    public class foodfetch extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    parseResult(response);
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("Exception", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            populateList();
            onListViewClick();
            spinner.setVisibility(View.GONE);
            /* Download complete. Lets update UI */
            /*if(result == 1){
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, blogTitles);
                ListView listView=(ListView)findViewById(R.id.list);
                listView.setAdapter(arrayAdapter);
            }else{
                Log.e("Failed", "Failed to fetch data!");
            }*/

            //spinner.setVisibility(View.GONE);

            /*profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
            profilePictureView.setProfileId(userId);*/
        }
    }
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }
    private void parseResult(String result) {
        try{

            Log.d("Bigjson","String="+result);
            JSONObject response = new JSONObject(result);
            Log.d("response","response="+response);
            JSONArray posts = response.optJSONArray("types");
            Log.d("posts","posts="+posts);
            types =new String[posts.length()];

            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.getJSONObject(i);
                Log.d("Jsonobject","Jsonobject="+post);

                //String title = post.optString("name").toString();

                types[i] = parseProperly(""+post,"type");
                //String istring=parseProperly(""+post,"thumb");
                //ImageList[i]=removeSlash(istring);
                //ImageList[i]=parseProperly("" + post, "icon");
                //Log.d("Names","Name="+ImageList[i]);
            }
        }catch (JSONException e){
            Log.d("Exception", "Error in Json parsing");
        }
    }
    private String parseProperly(String par,String key)
    {
        String result="";
        int i;
        int start=par.indexOf(key+"\":");
        i=start+3+key.length();
        while(par.charAt(i)!='\"')
        {
            result=result+par.charAt(i);
            i++;
        }
        //result=par.matches("name");
        return result;
    }
    private void onListViewClick() {
        ListView list=(ListView)findViewById(R.id.foodList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {
                String Clickedtype=types[position];
                /*List<Food> foodSelected=new ArrayList<Food>();
                int n=rest.foodList.size();
                int i;
                for(i=0;i<n;i++)
                {
                    if(food[position].equals(rest.foodList.get(i).getType()))
                    {
                        foodSelected.add(rest.foodList.get(i));
                    }
                }*/

                Intent intent=new Intent("com.example.ronak.hungrybaba.FoodDetails");
                intent.putExtra("hotelObject", rest);
                intent.putExtra("typeSelected", Clickedtype);
                intent.putExtra("hid", hid);
                startActivity(intent);
            }
        });

    }

    private void populateList() {
        //food= new String[]{"Pasta", "Sides"};
        /*String type;
        int n=rest.foodList.size();
        int i,j=0;
        for(i=0;i<n;i++) {
            type=rest.foodList.get(i).getType();
            if(!(contains(food,type,j))) {
                food[j] =type ;
                j++;
            }
        }*/
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,types);
        ListView list=(ListView)findViewById(R.id.foodList);
        list.setAdapter(adapter);
    }

    private boolean contains(String foodbag[],String type,int n) {
        int i;
        for(i=0;i<n;i++)
        {
            if(foodbag[i].equals(type))
            {
             return true;
            }
        }
        return false;
    }

}
