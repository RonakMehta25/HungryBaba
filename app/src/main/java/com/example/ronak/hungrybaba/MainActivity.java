package com.example.ronak.hungrybaba;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationAvailability;
import com.google.gson.Gson;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends  AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    public ImageLoaderConfiguration config;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    String[] blogTitles;
    String[] ImageList;
    String[] places;
    Bitmap profPict;
    String userId;
    private int[] hotelid;
    ProfilePictureView profilePictureView;
    private ProgressBar spinner;
    private List<restaurant> rest=new ArrayList<restaurant>();
    private List<restaurant> rest1=new ArrayList<restaurant>();
    private Menu placemenu;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //config = new ImageLoaderConfiguration.Builder(this).build();
        setContentView(R.layout.activity_main);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        //spinner.setVisibility(View.GONE);
        res = getResources();
        /*Activity frag=this;
        ActionBar ab =((AppCompatActivity)frag).getSupportActionBar();*/
        //userId=getIntent().getExtras().getString("userId");
        //ImageView propic=(ImageView)findViewById(R.id.profilePic);
        //new DownloadImageTask(propic).execute("");
        /*ImageLoader.getInstance().init(config);
        ImageLoader imageLoader=ImageLoader.getInstance();
        String plink="http://graph.facebook.com/"+userId+"/picture";
        imageLoader.displayImage(plink, propic);*/

        CreateSlider();

        /*try {
            creatingdatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        new AsyncHttpTask().execute("http://www.hungrybaba.esy.es/hotelfetch.php");
       // gettingplaces();
        //populateFood();

        //onListViewClick();
        String[] names={"Mac","Pizzahut","KFC"};
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(getListView().getContext(),android.R.sample.simple_list_item_1,names);
        //getListView().setAdapter(adapter);
    }

    private void CreateSlider() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        String[] values = new String[]{
                "Stop Animation (Back icon)",
                "Stop Animation (Home icon)",
                "Start Animation",
                "Change Color",
                "GitHub Page",
                "Share",
                "Rate"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
           /* String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;*/
            URL image_value = null;
            try {
                image_value = new URL("http://graph.facebook.com/"+userId+"/picture" );
                Log.d("UserId","UserId="+userId);
                profPict = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return profPict;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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
            populaterest();
            populateListView();
            onListViewClick();
            /* Download complete. Lets update UI */
            /*if(result == 1){
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, blogTitles);
                ListView listView=(ListView)findViewById(R.id.list);
                listView.setAdapter(arrayAdapter);
            }else{
                Log.e("Failed", "Failed to fetch data!");
            }*/

            spinner.setVisibility(View.GONE);
            int n=places.length;
            for(int i=0;i<n;i++)
            {
                MenuItem item = placemenu.add(Menu.NONE,1,Menu.NONE,places[i]);
            }
            //MenuItem item = placemenu.add(Menu.NONE,1,Menu.NONE,R.string.exitOption);
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
    private String removeSlash(String input)
    {
        String output="";
        for(int i=0;i<input.length();i++)
        {
            if(input.charAt(i)!='\\') {
                output = output + input.charAt(i);
            }
        }
        return output;
    }
    private void parseResult(String result) {
        try{

            Log.d("Bigjson","String="+result);
            JSONObject response = new JSONObject(result);
            Log.d("response","response="+response);
            JSONArray posts = response.optJSONArray("hotels");
            Log.d("posts","posts="+posts);
            blogTitles =new String[posts.length()];
            ImageList=new String[posts.length()];
            hotelid=new int[posts.length()];
            places=new String[posts.length()];
            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.getJSONObject(i);
                Log.d("Jsonobject","Jsonobject="+post);

                //String title = post.optString("name").toString();

                blogTitles[i] = parseProperly(""+post,"name");
                //String istring=parseProperly(""+post,"thumb");
                //ImageList[i]=removeSlash(istring);
                ImageList[i]=parseProperly("" + post, "icon");
                hotelid[i]=Integer.parseInt(parseProperly("" + post, "h_id"));
                places[i]=parseProperly(""+post,"address");
                //Log.d("Names","Name="+ImageList[i]);
            }
        }catch (JSONException e){
            Log.d("Exception", "Error in Json parsing");
        }
    }
    private void gettingplaces() {
        /*HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://developers.zomato.com/api/v2.1/dailymenu");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("apikey", "e702bf4164747e58fbb28e9295173f04"));
        pairs.add(new BasicNameValuePair("res_id", "16507624"));
        post.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = client.execute(post);*/
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }
    /*private void creatingdatabase() throws SQLException {
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }
        Cursor CR=myDbHelper.getInformation(myDbHelper);
        CR.moveToFirst();
        if(CR.getCount()>0&&CR != null) {

            do {
                //String nameicon="R.drawable."+CR.getString(3);
                //int iconid=Integer.parseInt(nameicon);
                //int d = getResources().getIdentifier(CR.getString(3), "drawable", getPackageName());
                int d=R.drawable.mc;
                rest.add(new restaurant(CR.getString(0), d, Float.parseFloat(CR.getString(2))));
            } while (CR.moveToNext());
        }
    }*/

    private void populateFood() {
        //rest1.add(new restaurant("Mac",""+R.drawable.mc,4));
       /* rest.get(0).insertFood("Cream Sauce Pasta", "Pasta", 120);
        rest.get(0).insertFood("Chicken Pasta","Pasta",140);
        rest.get(0).insertFood("Plain Garlic Bread","Sides",70);
        rest.get(0).insertFood("Chicken Wings","Sides",135);
        rest.get(0).insertFood("Potato Wedges","Sides",135);
        rest.get(1).insertFood("Cream Sauce Pasta", "Pasta", 120);
        rest.get(1).insertFood("Chicken Pasta","Pasta",140);
        rest.get(1).insertFood("Plain Garlic Bread","Sides",70);
        rest.get(1).insertFood("Chicken Wings","Sides",135);
        rest.get(1).insertFood("Potato Wedges","Sides",135);
        rest.get(2).insertFood("Cream Sauce Pasta", "Pasta", 120);
        rest.get(2).insertFood("Chicken Pasta","Pasta",140);
        rest.get(2).insertFood("Plain Garlic Bread","Sides",70);
        rest.get(2).insertFood("Chicken Wings","Sides",135);
        rest.get(2).insertFood("Potato Wedges","Sides",135);*/
    }

    private void onListViewClick() {
         ListView list=(ListView)findViewById(R.id.list);
         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {
                restaurant clickedRest=rest.get(position);
                //String msg="Hello you clicked on "+clickedRest.getName();
                //Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                Intent intent=new Intent("com.example.ronak.hungrybaba.RestaurantDetails");
                intent.putExtra("hotelObject",clickedRest);
                //intent.putExtra("hotelName",clickedRest.getName());
                intent.putExtra("hid",clickedRest.gethid());
                SharedPreferences preferences = getSharedPreferences("orders", MODE_PRIVATE);
                preferences.edit().remove("order").commit();
                Gson gson = new Gson();
                SharedPreferences mPrefs = getSharedPreferences("hotels", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String json = gson.toJson(clickedRest.getName());
                Log.d("json","json="+json);
                prefsEditor.putString("hotel", json);
                prefsEditor.commit();
                startActivity(intent);
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<restaurant> adapter=new MyListAdapter();
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private void populaterest() {
        /*rest.add(new restaurant("Mac",R.drawable.mc,5));
        rest.add(new restaurant("Pizzahut",R.drawable.pizzahut,4));
        rest.add(new restaurant("KFC",R.drawable.kfc,4));*/
        int size=blogTitles.length;
        for(int i=0;i<size;i++)
        {
            rest.add(new restaurant(hotelid[i],blogTitles[i],ImageList[i],4));
        }

    }

    @Override


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem item = menu.add(Menu.NONE,1,Menu.NONE,R.string.exitOption);
        placemenu=menu;
        return true;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private class MyListAdapter extends ArrayAdapter<restaurant> {

        public MyListAdapter() {
            super(MainActivity.this, R.layout.sample,rest);

        }

        @Override
        public View getView(int position,View contentView,ViewGroup parent)
        {
            View itemView=contentView;
            if(itemView==null)
            {
              itemView=getLayoutInflater().inflate(R.layout.sample, parent, false);
            }
            restaurant myrest=rest.get(position);
            ImageView imageView=(ImageView)itemView.findViewById(R.id.item_icon);
            //new DownloadImageTask(imageView).execute(myrest.getIcon_id());
            //ImageLoader.getInstance().init(config);
            //ImageLoader imageLoader=ImageLoader.getInstance();
            //imageLoader.displayImage(myrest.getIcon_id(), imageView);
            //imageView.setImageResource(R.drawable.mc);
            int resID = res.getIdentifier(myrest.getIcon_id() , "drawable", getPackageName());
            //Drawable drawable = res.getDrawable(resID );
            imageView.setImageResource(resID);



            TextView restText=(TextView) itemView.findViewById(R.id.item_name);
            restText.setText(myrest.getName());

            RatingBar rate=(RatingBar)itemView.findViewById(R.id.ratingBar);
            rate.setRating(myrest.getRating());
            return itemView;
             //return  super.getView(position,contentView,parent);
        }
    }
}
