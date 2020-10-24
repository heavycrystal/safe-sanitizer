package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterPhase3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase3);
    }
    public void returnback_action(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}