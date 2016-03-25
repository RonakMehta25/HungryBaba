package com.example.ronak.hungrybaba;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronak on 11-02-2016.
 */
public class restaurant implements Serializable {
    private String name;
    private String icon_id;
    private float rating;
    private int hid;
    public List<Food> foodList=new ArrayList<Food>();
    public restaurant(int hid,String name, String icon_id,float rating) {
        this.hid=hid;
        this.name = name;
        this.icon_id = icon_id;
        this.rating=rating;
    }
  /*  public void insertFood(String name,String type,int price)
    {
        foodList.add(new Food(name,type,price,1));
    }*/
    public int gethid() {
        return hid;
    }
    public String getIcon_id() {
        return icon_id;
    }

    public float getRating()
    {
        return rating;
    }
    public String getName() {
        return name;
    }
}
