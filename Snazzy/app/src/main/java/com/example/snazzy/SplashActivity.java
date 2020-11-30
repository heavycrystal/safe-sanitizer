package com.example.snazzy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent i = new Intent(com.example.snazzy.SplashActivity.this, MainActivity_Todo.class);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            startActivity(i);
            finish();
        }, 5000);
    }
}