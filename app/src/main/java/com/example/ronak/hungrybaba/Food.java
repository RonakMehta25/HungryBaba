package com.example.ronak.hungrybaba;

import java.io.Serializable;

/**
 * Created by Ronak on 21-02-2016.
 */
public class Food implements Serializable{
    //private String type;
    private String name;
    private int price;
    int count;

    public Food(String name, int price,int count) {
        this.name = name;
        this.price = price;
        this.count=count;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    /*public String getType() {
        return type;
    }*/



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
