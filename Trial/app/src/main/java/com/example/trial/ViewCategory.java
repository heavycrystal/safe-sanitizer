package com.example.trial;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ViewCategory extends AppCompatActivity {
    static Intent intent;
    Map<String, ItemData> itemMap = new HashMap<>();
    ValueEventListener listener;
    DatabaseReference db;
    public String CATEGORY;
    public String ADD_SUCCESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        intent = getIntent();
        CATEGORY = intent.getStringExtra(MainActivity.MESSAGE);
        ADD_SUCCESS = intent.getStringExtra(AddItemActivity.MESSAGE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setTag(CATEGORY);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });
        if(ADD_SUCCESS!=null)
            Toast.makeText(this, "Item "+ADD_SUCCESS+" added successfully", Toast.LENGTH_LONG).show();
        listener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    itemMap.put(child.getKey(), new ItemData(child.child("price").getValue(Double.class), child.child("quantity").getValue(Integer.class)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        db = FirebaseDatabase.getInstance().getReference("/"+MainActivity.USER+"/"+CATEGORY);
        db.addValueEventListener(listener);
        }

    public void addItem(View view){
        intent = new Intent(this, AddItemActivity.class);
        intent.putExtra(MainActivity.MESSAGE, (String) view.getTag());
        startActivity(intent);
    }
}