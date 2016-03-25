package com.example.ronak.hungrybaba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class FoodQuantity extends AppCompatActivity {
     int count=1;
    String foodname;
    int foodprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_quantity);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        foodprice=getIntent().getIntExtra("foodprice", 1);
        foodname= getIntent().getStringExtra("foodname");
        TextView text=(TextView)findViewById(R.id.FoodName);
        text.setText(foodname);
        TextView tex=(TextView)findViewById(R.id.price);
        tex.setText(""+foodprice);
    }

    public void itemClicked(View v) {
        int n,p;
        switch (v.getId()) {
            case R.id.plus:
                  n=count;
                  n++;
               // foodClicked.setCount(n);
                count=n;
                TextView quant=(TextView)findViewById(R.id.quant);
                quant.setText("Quantity " + n);
                TextView tex=(TextView)findViewById(R.id.price);
                p=Integer.parseInt(tex.getText().toString());
                p=p+foodprice;
                tex.setText(""+p);
                //doSomething1();
                break;
            case R.id.minus:
                n = count;
                if(n>1) {

                    n--;
                    count=n;
                    TextView quant1 = (TextView) findViewById(R.id.quant);
                    quant1.setText("Quantity " + n);
                    TextView tex1=(TextView)findViewById(R.id.price);
                    p=Integer.parseInt(tex1.getText().toString());
                    p=p-foodprice;
                    tex1.setText(""+p);
                }
                //doSomething2();
                break;
        }
    }
    public void cartClicked(View v)
    {
        order foodorder=new order();
        //order foodorder=new order();
        //foodorder.insertFood(foodname,foodprice,count);
        /*ImageButton cart=new ImageButton(this);
        cart.setImageResource(R.drawable.darkgreencart1);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.FoodQuantity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        //params.rightMargin = 50;
        //params.bottomMargin = 60;
        rl.addView(cart, params);*/
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("orders", MODE_PRIVATE);
        String json1 = mPrefs.getString("order", "");

        if(json1!="") {
            order obj = gson.fromJson(json1, order.class);
            obj.insertFood(foodname,foodprice,count);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            String json = gson.toJson(obj);
            Log.d("json","json="+json);
            prefsEditor.putString("order", json);
            prefsEditor.commit();
        }
        else
        {
            foodorder.insertFood(foodname,foodprice,count);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            String json = gson.toJson(foodorder);
            Log.d("json","json="+json);
            prefsEditor.putString("order", json);
            prefsEditor.commit();
        }


        Toast.makeText(FoodQuantity.this, "Ordered", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }



}
