package com.example.firebasetest;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import java.util.Objects;
import java.util.regex.Pattern;

public class PwordcheckActivity extends AppCompatActivity {
    //at least 1 digit
    //at least 1 lower case letter
    // at least 1 upper case letter
    //any letter
    //at least 1 special character
    //no white spaces
    //at least 4 characters
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{6,}" + "$"); //mad BT yo

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwordcheck);

        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
    }

    private boolean validateEmail() {
        String emailInput = Objects.requireNonNull(textInputEmail.getEditText()).getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = Objects.requireNonNull(textInputUsername.getEditText()).getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty"); //to ensure usename isn't null. Add a regex if you want
            return false;
        }
        else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long"); //this is just a number for length of username.
            return false;
        }
        else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = Objects.requireNonNull(textInputPassword.getEditText()).getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("This field can't be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword())
            return;

        String input = "Email: " + Objects.requireNonNull(textInputEmail.getEditText()).getText().toString();
        input += "\n";
        input += "Username: " + Objects.requireNonNull(textInputUsername.getEditText()).getText().toString();
        input += "\n";
        input += "Password: " + Objects.requireNonNull(textInputPassword.getEditText()).getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }
}