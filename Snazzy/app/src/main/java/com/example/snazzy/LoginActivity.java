package com.example.snazzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    public static String USERNAME;
    public static String PROFESSION = SendgridTestActivity.PROFESSION;
    private String userPassword;
    DBHelper db;
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
            userEmail=currentUser.getEmail();
            db=new DBHelper(getApplicationContext());
            USERNAME = db.getUsername(userEmail);
            Toast.makeText(this, "Signing in current user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
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
        db = new DBHelper(getApplicationContext());
        return !db.checkUniqueUser(userName);
    }

    public void login_action(View view) {
        userEmail = ((EditText) findViewById(R.id.emaild_input)).getText().toString();
        USERNAME = ((EditText) findViewById(R.id.usernamec_input)).getText().toString();
        userPassword = ((EditText) findViewById(R.id.passwordc_input)).getText().toString();
        Log.d("LOGIN ACTION", "login action");

        if (!username_validity(USERNAME)) {
            Log.d("CHECK", "validity check");
            Toast.makeText(this, "Username not found.", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(userEmail,
                sha256(USERNAME + userPassword))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            userEmail = currentUser.getEmail();
                            db = new DBHelper(getApplicationContext());
                            MainActivity.USERNAME = db.getUsername(userEmail);
                            MainActivity.PROFESSION = db.getProf(MainActivity.USERNAME);

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}