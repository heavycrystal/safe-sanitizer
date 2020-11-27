package com.example.firebasetest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPhase1NActivity extends AppCompatActivity {

    String userEmail;
    public static String userName;
    String userPassword;
    String userCpassword;
    boolean input_val_auth_error = false;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase1_n);
    }

    protected boolean checkUniqueUsername(String user_name)
    {
        // To be filled by Ankita.
        return true;
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    View.OnClickListener verify_phase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseAuth.signInWithEmailAndPassword(userEmail,
                    sha256(userName + userPassword));
            if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {
                Toast.makeText(RegisterPhase1NActivity.this, "Verified!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterPhase1NActivity.this, RegisterPhase1Activity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(RegisterPhase1NActivity.this, "E-mail address not verified yet.", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener resend_phase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseAuth.signInWithEmailAndPassword(userEmail,
                    sha256(userName + userPassword));
            Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ((Button)findViewById(R.id.register_phase1_button)).setText("Continue");
                            new CountDownTimer(60000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    ((TextView)findViewById(R.id.countdown)).setText("Resend available in " + millisUntilFinished / 1000 + " seconds.");
                                }

                                public void onFinish() {
                                    ((TextView)findViewById(R.id.countdown)).setText("Resend available.");
                                    ((Button)findViewById(R.id.register_phase1_button)).setText("Resend");
                                    ((Button)findViewById(R.id.register_phase1_button)).setOnClickListener(resend_phase);
                                }
                            }.start();
                            ((Button)findViewById(R.id.register_phase1_button)).setOnClickListener(verify_phase);
                        }
                    });
        }
    };

    public void Phase1RegisterAction(View view) {

        userEmail = ((EditText)findViewById(R.id.email_input)).getText().toString();
        userName = ((EditText)findViewById(R.id.user_input)).getText().toString();
        userPassword = ((EditText)findViewById(R.id.password_input)).getText().toString();
        userCpassword = ((EditText)findViewById(R.id.cpassword_input)).getText().toString();

        firebaseAuth = FirebaseAuth.getInstance();

        if(userEmail.length() == 0)
        {
            ((EditText)findViewById(R.id.email_input)).setError("Enter a email.");
            input_val_auth_error = true;
        }
        if(userName.length() == 0)
        {
            ((EditText)findViewById(R.id.user_input)).setError("Enter a username.");
            input_val_auth_error = true;
        }
        if(!checkUniqueUsername(userName))
        {
            ((EditText)findViewById(R.id.user_input)).setError("This username is already in use, please enter a different one.");
            input_val_auth_error = true;
        }
        if(userPassword.length() < 6)
        {
            ((EditText)findViewById(R.id.password_input)).setError("Enter a password with atleast 6 characters.");
            input_val_auth_error = true;
        }
        if(!userPassword.equals(userCpassword))
        {
            ((EditText)findViewById(R.id.password_input)).setError("Passwords do not match.");
            input_val_auth_error = true;
        }
        if(input_val_auth_error)
        {
            input_val_auth_error = false;
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(userEmail,
                sha256(userName + userPassword))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterPhase1NActivity.this, "Registered successfully. Please check your email for verification",
                                                        Toast.LENGTH_LONG).show();
                                                ((Button)findViewById(R.id.register_phase1_button)).setText("Continue");
                                                new CountDownTimer(60000, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        ((TextView)findViewById(R.id.countdown)).setText("Resend available in " + millisUntilFinished / 1000 + " seconds.");
                                                    }

                                                    public void onFinish() {
                                                        ((TextView)findViewById(R.id.countdown)).setText("Resend available.");
                                                        ((Button)findViewById(R.id.register_phase1_button)).setText("Resend");
                                                        ((Button)findViewById(R.id.register_phase1_button)).setOnClickListener(resend_phase);
                                                    }
                                                }.start();
                                                ((Button)findViewById(R.id.register_phase1_button)).setOnClickListener(verify_phase);
                                            } else {
                                                Toast.makeText(RegisterPhase1NActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterPhase1NActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
