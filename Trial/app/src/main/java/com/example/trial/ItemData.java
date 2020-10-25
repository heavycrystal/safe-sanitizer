package com.example.trial;

public class ItemData{
    double price;
    int quantity;
    String imagePath;
    ItemData(){
    }
    ItemData(double price, int quantity){
        this.price = price;
        this.quantity = quantity;
    }
    public double getPrice(){
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void changePrice(int new_price){
        this.price = new_price;
    }
    public void changeQuantity(int new_quantity){
        this.quantity = new_quantity;
    }
}