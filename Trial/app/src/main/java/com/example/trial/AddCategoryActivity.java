package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//User uploads an image, its path is set as value for the new category
public class AddCategoryActivity extends AppCompatActivity {
    EditText et;
    DatabaseReference db;
    Intent intent;
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
    }
    public void sendName(View view){
        intent = new Intent(this, Dashboard.class);
        et = (EditText) findViewById(R.id.addCat);
        String newCategory = et.getText().toString();
        db = FirebaseDatabase.getInstance().getReference("/"+MainActivity.USER+"/categories");        intent.putExtra(MainActivity.MESSAGE, newCategory);
        db.child(newCategory).setValue(imagePath);
        intent.putExtra(MainActivity.MESSAGE, newCategory);
        startActivity(intent);
    }
}