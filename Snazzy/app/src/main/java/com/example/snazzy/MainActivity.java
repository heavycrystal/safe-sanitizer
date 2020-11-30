package com.example.snazzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button dash_but;
    public static String USERNAME="Stranger";
    public static String PROFESSION="Homemaker";
    public static String MESSAGE = "MESSAGE";
    public static MediaPlayer player = null;
    Button loginbtn, regbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper db = new DBHelper(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            Log.e("EMAIL", userEmail);
            db = new DBHelper(getApplicationContext());
            USERNAME = db.getUsername(userEmail);
            PROFESSION = db.getProf(USERNAME);
            Toast.makeText(this, "Welcome back "+USERNAME+"!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }

        loginbtn= findViewById(R.id.login_button);
        regbtn=findViewById(R.id.registration_button);

        regbtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SendgridTestActivity.class);
            startActivity(intent);
        });

        loginbtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}