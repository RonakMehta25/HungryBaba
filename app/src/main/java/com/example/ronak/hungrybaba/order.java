package com.example.ronak.hungrybaba;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronak on 22-03-2016.
 */
public class order implements Serializable {
    public List<Food> foodList=new ArrayList<Food>();
    public void insertFood(String name,int price,int count)
    {
        foodList.add(new Food(name,price,count));
    }
}
