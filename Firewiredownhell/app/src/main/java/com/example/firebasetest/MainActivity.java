package com.example.firebasetest;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void loginAction(View view) {
        //Snackbar.make(findViewById(android.R.id.content), "Not implemented yet.", Snackbar.LENGTH_LONG).show();
       /* NotifSender bleh = new NotifSender();
        Intent i = new Intent();
        bleh.onReceive(this, i); */
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void MainRegisterAction(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}