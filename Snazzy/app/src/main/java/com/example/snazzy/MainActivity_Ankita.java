package com.example.snazzy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_Ankita extends AppCompatActivity {
    public static final String MESSAGE = "com.example.snazzy.MESSAGE";
    public static String USER = "user1"; //updated on login
    private static final String TAG = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ankita);
        //directly goes to dashboard
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAction(View view){

    }
}