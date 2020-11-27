package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    String userEmail;
    public static String userName;
    String userPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null)
        {
            Toast.makeText(this, "Signing in current user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected boolean username_validity(String userName) {
        // Hi Ankita!
        return true;
    }

    public void login_action(View view) {
        userEmail = ((EditText) findViewById(R.id.emailc_input)).getText().toString();
        userName = ((EditText) findViewById(R.id.usernamec_input)).getText().toString();
        userPassword = ((EditText) findViewById(R.id.passwordc_input)).getText().toString();

        if (!username_validity(userName)) {
            Toast.makeText(this, "Username not found.", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(userEmail,
                sha256(userName + userPassword))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            }
                        else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}