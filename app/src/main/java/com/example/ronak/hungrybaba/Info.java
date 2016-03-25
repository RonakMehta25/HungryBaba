package com.example.ronak.hungrybaba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    public class registerfood extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
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
            Intent intent = new Intent("com.example.ronak.hungrybaba.Final");
            startActivity(intent);
        }
    }
    public void register(View v)
    {
        EditText name=(EditText)findViewById(R.id.name);
        String Name=name.getText().toString();
        EditText email=(EditText)findViewById(R.id.email);
        String Email=email.getText().toString();
        EditText mno=(EditText)findViewById(R.id.mno);
        String Mno=mno.getText().toString();
        EditText address=(EditText)findViewById(R.id.address);
        String Address=address.getText().toString();
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("orders", MODE_PRIVATE);
        String hname = mPrefs.getString("hotels", "");
        String furl="hungrybaba.esy.es/registerorder.php?hname="+hname+"&pname="+Name+"&email="+Email+"&phone="+Mno+"&address="+Address+"&food='pasta'";
        new registerfood().execute(furl);
    }


}
