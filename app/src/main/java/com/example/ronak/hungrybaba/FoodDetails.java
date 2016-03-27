package com.example.ronak.hungrybaba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.List;

public class FoodDetails extends AppCompatActivity {
    private  restaurant rest;
    private String type;
    private ProgressBar spinner;
    private int hid;
    Resources res;
    String[] foodname;
    int[] price;
    List<Food> foodSelected=new ArrayList<Food>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
       /* foodSelected.add(new Food("Cream Sauce Pasta", "Pasta", 120, 1));
        foodSelected.add(new Food("Chicken Pasta", "Pasta", 140, 1));
        foodSelected.add(new Food("Plain Garlic Bread", "Sides", 70, 1));
        foodSelected.add(new Food("Chicken Wings","Sides",135,1));
        foodSelected.add(new Food("Potato Wedges","Sides",135,1));*/
        //foodSelected=(ArrayList<Food>)getIntent().getSerializableExtra("foodSelected");
        rest=(restaurant)getIntent().getSerializableExtra("hotelObject");
        type=getIntent().getStringExtra("typeSelected");
        hid=getIntent().getIntExtra("hid", 1);
        //rest=(restaurant)getIntent().getSerializableExtra("hotelObject");


        ImageView hotelImage=(ImageView)findViewById(R.id.HotelImage2);
        //hotelImage.setImageResource(R.drawable.mc);
        res = getResources();
        int resID = res.getIdentifier(rest.getIcon_id() , "drawable", getPackageName());
        hotelImage.setImageResource(resID);
        TextView text=(TextView)findViewById(R.id.Nametext2);
        text.setText(rest.getName());

        String furl="http://www.hungrybaba.esy.es/foodfetchname.php?hid="+hid+"&type='"+type+"'";
        new foodfetchname().execute(furl);
        //populateList();
        //onListViewClick();
    }
    public class foodfetchname extends AsyncTask<String, Void, Integer> {

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
            JSONArray posts = response.optJSONArray("foods");
            Log.d("posts","posts="+posts);
            foodname =new String[posts.length()];
            price=new int[posts.length()];
            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.getJSONObject(i);
                Log.d("Jsonobject","Jsonobject="+post);

                //String title = post.optString("name").toString();

                foodname[i] = parseProperly(""+post,"fname");
                price[i] =Integer.parseInt(parseProperly(""+post,"price"));
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
        ListView list=(ListView)findViewById(R.id.foodDetailsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {

                //Food foodClicked = foodSelected.get(position);
                String foodClicked=foodname[position];
                int clickedprice=price[position];
                Intent intent = new Intent("com.example.ronak.hungrybaba.FoodQuantity");
                intent.putExtra("foodname", foodClicked);
                intent.putExtra("foodprice", clickedprice);
                startActivity(intent);
            }
        });
    }
    private void populateList() {
        ArrayAdapter<String> adapter=new MyListAdapter();
        ListView list=(ListView)findViewById(R.id.foodDetailsList);
        list.setAdapter(adapter);
    }
    public void cartFinal(View v)
    {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("orders", MODE_PRIVATE);
        String json = mPrefs.getString("order", "");
        Log.d("json","json="+json);


        if(json!="") {
            order obj = gson.fromJson(json, order.class);
            Intent intent = new Intent("com.example.ronak.hungrybaba.bill");
            intent.putExtra("order",obj);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(FoodDetails.this,"You have not ordered anything",Toast.LENGTH_LONG).show();
        }
    }
    private class MyListAdapter extends ArrayAdapter<String> {

        public MyListAdapter() {
            super(FoodDetails.this, R.layout.foodprice,foodname);

        }

        @Override
        public View getView(int position,View contentView,ViewGroup parent)
        {
            View itemView=contentView;
            if(itemView==null)
            {
                itemView=getLayoutInflater().inflate(R.layout.foodprice, parent, false);
            }
            //Food myfood=foodSelected.get(position);
            String myfood=foodname[position];
            TextView restText1=(TextView) itemView.findViewById(R.id.textViewfood);
            restText1.setText(myfood);

            TextView restText2=(TextView) itemView.findViewById(R.id.textViewPrice);
            restText2.setText(""+price[position]);


            return itemView;
            //return  super.getView(position,contentView,parent);
        }
    }

}
