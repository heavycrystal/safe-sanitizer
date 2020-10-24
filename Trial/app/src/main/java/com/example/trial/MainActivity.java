package com.example.trial;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ItemData{
    double price;
    int quantity;
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

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "com.example.trial.MESSAGE";
    private static final String TAG = null;

    //DBHelper helper;
    SQLiteDatabase mydb;
    DBHelper helper;
    Map<String, ItemData> item = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAction(View view){

    }
}