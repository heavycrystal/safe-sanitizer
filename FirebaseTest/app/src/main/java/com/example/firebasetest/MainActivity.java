package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void login_action(View view) {
        Snackbar.make(view, "Login not implemented yet.", Snackbar.LENGTH_LONG)
                .show();
    }

    public void register_action(View view) {
        Intent intent = new Intent(this, RegisterPhase1Activity.class);
        startActivity(intent);
    }
}