package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Added item: "+message);
        intent = new Intent(this, Dashboard.class);
        intent.putExtra(MainActivity.MESSAGE, "Welcome back!");
        startActivity(intent);
    }
}