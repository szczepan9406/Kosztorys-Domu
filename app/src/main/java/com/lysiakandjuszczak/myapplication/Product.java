package com.lysiakandjuszczak.myapplication;

/**
 * Created by dawid on 24.05.17.
 */

public class Product {

    private String name;
    private double prize;
    private String category;
    private int count;

    public Product(String name, double prize, int count, String category) {
        this.name = name;
        this.prize = prize;
        this.category = category;
        this.count = count;
    }

    public Product(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
