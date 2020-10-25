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

public class AddItemActivity extends AppCompatActivity {
    EditText viewItemName, viewPrice, viewQuantity;
    String itemName;
    public String CATEGORY;
    public static String MESSAGE;
    double price;
    int quantity;
    ItemData itemData;
    Intent intent;
    private DatabaseReference db;
    private Map<String, ItemData> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
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
        intent = getIntent();
        CATEGORY = intent.getStringExtra(MainActivity.MESSAGE);
        db = FirebaseDatabase.getInstance().getReference();
        map.put(itemName, itemData);
        db.child(MainActivity.USER).child(CATEGORY).child(itemName).setValue(itemData);
        String newItem = itemName;
        intent = new Intent(this, ViewCategory.class);
        intent.putExtra(AddItemActivity.MESSAGE, newItem);
        startActivity(intent);
    }
}