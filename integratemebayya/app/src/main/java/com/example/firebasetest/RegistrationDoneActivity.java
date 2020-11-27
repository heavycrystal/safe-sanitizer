package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationDoneActivity extends AppCompatActivity {

    private String userName;
    private int OTP;
    private String userEmail;
    private String userPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser phoneAccount;
    AuthCredential emailAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_done);

        userName = getIntent().getStringExtra("userName");
        OTP = getIntent().getIntExtra("OTP", 123456);
        userEmail = getIntent().getStringExtra("userEmail");
        userPassword = getIntent().getStringExtra("userPassword");

        String display_text = "Your account with Tedium has been successfully registered: "
                + "\n\t"
                + "Username: "
                + userName
                + "\n\t"
                + "User E-mail ID: "
                + userEmail
                + "\n\t"
                + "We hope you enjoy using Tedium!";
        ((TextView)findViewById(R.id.completion_text)).setText(display_text);

        firebaseAuth = FirebaseAuth.getInstance();
        phoneAccount = firebaseAuth.getCurrentUser();

        // Ankita can add username to DB here.
    }

    public void goto_main(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}