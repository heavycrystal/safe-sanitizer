package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Category1 extends AppCompatActivity {
    EditText viewItemName, viewPrice, viewQuantity;
    String itemName;
    String MESSAGE="vhgbjhnjkmkl";
    double price;
    int quantity;
    ItemData itemData;
    private DatabaseReference db;
    private Map<String, ItemData> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category1);
    }
    public void addItem(View view){
        {
            viewItemName=(EditText) findViewById(R.id.enterItemName);
            viewPrice=(EditText) findViewById(R.id.enterPrice);
            viewQuantity=(EditText) findViewById(R.id.enterQuantity);
            itemName=viewItemName.getText().toString();
            price=Double.parseDouble(viewPrice.getText().toString());
            quantity=Integer.parseInt(viewQuantity.getText().toString());
            itemData = new ItemData(price, quantity);

        }
        Intent intent = new Intent(this, MainActivity.class);
        db = FirebaseDatabase.getInstance().getReference();
        map.put(itemName, itemData);
        db.child("user1").child((String) view.getTag()).child(itemName).setValue(itemData);
        String message = itemName;
        intent.putExtra(MESSAGE, message);
        startActivity(intent);
        finish();

    }
}