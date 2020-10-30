package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendgridTestActivity extends AppCompatActivity {

    public static String userEmail;
    public static String userName;
    public static String userPassword;
    String userCpassword;
    boolean input_val_auth_error = false;
    public static int OTP;
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("\\w*[-!$%^&*()_+|~=`{}\\[\\]:\";'<>?,.\\/]+\\w*");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" + "(?=.*[a-zA-Z])" + "(?=.*[-!$%^&*()_+|~=`{}\\[\\]:\";'<>?,.\\/])" + "(?=\\S+$)" + ".{6,}" + "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendgrid_test);
    }

    protected boolean checkUniqueUsername(String user_name)
    {
        // To be filled by Ankita.
        return true;
    }

    public void register_action(View view) {

        userEmail = ((EditText)findViewById(R.id.emaild_input)).getText().toString();
        userName = ((EditText)findViewById(R.id.userd_input)).getText().toString();
        userPassword = ((EditText)findViewById(R.id.passwordd_input)).getText().toString();
        userCpassword = ((EditText)findViewById(R.id.cpasswordd_input)).getText().toString();

        if(userEmail.length() == 0)
        {
            ((EditText)findViewById(R.id.emaild_input)).setError("Enter a email.");
            input_val_auth_error = true;
        }
        if(userName.length() == 0)
        {
            ((EditText)findViewById(R.id.userd_input)).setError("Enter a username.");
            input_val_auth_error = true;
        }
        if(USERNAME_PATTERN.matcher(userName).matches())
        {
            ((EditText)findViewById(R.id.userd_input)).setError("Username can only contain alphabets or letters.");
            input_val_auth_error = true;
        }
        if(!checkUniqueUsername(userName))
        {
            ((EditText)findViewById(R.id.userd_input)).setError("This username is already in use, please enter a different one.");
            input_val_auth_error = true;
        }
        if(!PASSWORD_PATTERN.matcher(userPassword).matches())
        {
            ((EditText)findViewById(R.id.passwordd_input)).setError("Password too weak.");
            input_val_auth_error = true;
        }
        if(!userPassword.equals(userCpassword))
        {
            ((EditText)findViewById(R.id.passwordd_input)).setError("Passwords do not match.");
            input_val_auth_error = true;
        }
        if(input_val_auth_error)
        {
            input_val_auth_error = false;
            return;
        }

        Random rand = new Random();
        OTP = 100000 + rand.nextInt(900000);
        SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SendEmailService.getInstance(getApplicationContext()).SendOTPEmail(userEmail, OTP);
            }
        });

        Toast.makeText(this, "An OTP was dispatched to " + userEmail, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, RegisterPhase3Activity.class);
        startActivity(intent);
    }

}