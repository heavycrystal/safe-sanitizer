package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class Dashboard extends AppCompatActivity {
    Intent intent;
    static Map<String, String> categoryMap;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        intent = getIntent();
        categoryMap = new HashMap<>();
        String newCategory = intent.getStringExtra(MainActivity.MESSAGE);
        if(newCategory!=null) {
            ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.layout);
            Button btn = new Button(this);
            btn.setText("Btn");
            btn.setTag(newCategory);
            btn.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            ll.addView(btn);
        }
        //Add all categories to categoryMap
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("/"+MainActivity.USER+"/categories");
        Log.d("trial", "got database");
        Button btn = new Button(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.removeAllViews();
        params.width = 200;
        params.height = 200;
        //for every category, create an image button
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //have to do operations here
                    String category = child.getKey();
                    categoryMap.put(category, child.child("image").getValue(String.class));
                    Log.d("trial", "inside for-each");
                    Log.d("trial", "**"+child.getKey()+"**");
                    Button btn = new Button(getApplicationContext());
                    btn.setTag(category);
                    btn.setLayoutParams(params);
                    btn.setText(category);
                    btn.setOnClickListener(view -> goToCategory(view));
                    ll.addView(btn);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("trial", "database error");
            }
        };
        db.addValueEventListener(listener);

    }
    public void goToCategory(View view){
        intent = new Intent(this, ViewCategory.class);
        intent.putExtra(MainActivity.MESSAGE, (String)view.getTag());
        startActivity(intent);
    }
    public void addCategory(View view){
        intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);
    }

}