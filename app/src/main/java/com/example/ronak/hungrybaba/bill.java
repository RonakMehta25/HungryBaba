package com.example.ronak.hungrybaba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class bill extends AppCompatActivity {
    //private order ord;
    private order ord;
    int total=0;
    private List<Food> foods=new ArrayList<Food>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ord=(order)getIntent().getSerializableExtra("order");
        foods=ord.foodList;
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        populateList();
        TextView totalprice=(TextView)findViewById(R.id.totalprice);
        int n=foods.size();
        for(int i=0;i<n;i++)
        {
            total=total+foods.get(i).getPrice()*foods.get(i).getCount();
            Log.d("price", "price=" + foods.get(i).getPrice());
        }
        totalprice.setText(""+total);
    }
   private void populateList() {
        ArrayAdapter<Food> adapter=new MyListAdapter();
        ListView list=(ListView)findViewById(R.id.billlist);
        list.setAdapter(adapter);
    }
    public void orderfunc(View v)
    {
        Intent intent = new Intent("com.example.ronak.hungrybaba.Info");
        startActivity(intent);
    }
    private class MyListAdapter extends ArrayAdapter<Food> {

        public MyListAdapter() {
            super(bill.this, R.layout.foodprice,foods);

        }

        @Override
        public View getView(int position,View contentView,ViewGroup parent)
        {
            View itemView=contentView;
            if(itemView==null)
            {
                itemView=getLayoutInflater().inflate(R.layout.foodprice, parent, false);
            }
            Food myfood=foods.get(position);
            String foodname=myfood.getName(); //foodname[position];
            TextView restText1=(TextView) itemView.findViewById(R.id.textViewfood);
            restText1.setText(foodname);

            TextView restText2=(TextView) itemView.findViewById(R.id.textViewPrice);
            int myprice=myfood.getPrice();
            restText2.setText("" + myprice+"*"+myfood.getCount());

           // total=total+myfood.getCount()*myfood.getPrice();

            return itemView;
            //return  super.getView(position,contentView,parent);
        }
    }

}
