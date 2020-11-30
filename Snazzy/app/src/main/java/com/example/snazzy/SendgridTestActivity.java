package com.example.snazzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.regex.Pattern;

public class SendgridTestActivity extends AppCompatActivity {

    public static String userEmail;
    public static String userName;
    public static String userPassword;
    public static String PROFESSION;
    String userCpassword;
    RadioGroup radioGroup;
    DBHelper db;
    boolean input_val_auth_error = false;
    public static int OTP;
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile(".*[-!$%^&*@()_+|~=`{}\\[\\]:\";'<>?,.\\/]+");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" + "(?=.*[a-zA-Z])" + "(?=.*[-!$%^&*()_+|~=`{}\\[\\]:\";'<>?,.\\/])" + "(?=\\S+$)" + ".{6,}" + "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendgrid_test);
        db = new DBHelper((getApplicationContext()));
        db.addUser("anki", "anki@mail.com", "1234567890", "Student");
    }

    protected boolean checkUniqueUsername(String user_name)
    {
        return db.checkUniqueUser(user_name);
    }
    public void register_action(View view) {

        userEmail = ((EditText)findViewById(R.id.emailid_input)).getText().toString();
        userName = ((EditText)findViewById(R.id.username_input)).getText().toString();
        userPassword = ((EditText)findViewById(R.id.pwd_input)).getText().toString();
        userCpassword = ((EditText)findViewById(R.id.cnfpwd_input)).getText().toString();
        radioGroup = findViewById(R.id.radio_grp);


        int prof=radioGroup.getCheckedRadioButtonId();
        if(prof==R.id.stud)
            PROFESSION = "Student";
        else if(prof==R.id.prof)
            PROFESSION = "Professional";
        else if(prof==R.id.homeM)
            PROFESSION = "Homemaker";

        if(userEmail.length() == 0)
        {
            ((EditText)findViewById(R.id.emailid_input)).setError("Enter an email.");
            input_val_auth_error = true;
        }
        if(!Pattern.matches(".+[@].+[.].+", userEmail))
        {
            ((EditText)findViewById(R.id.emailid_input)).setError("Enter a valid email");
            input_val_auth_error = true;
        }
        if(userName.length() == 0)
        {
            ((EditText)findViewById(R.id.username_input)).setError("Enter a username.");
            input_val_auth_error = true;
        }
        if(USERNAME_PATTERN.matcher(userName).matches())
        {
            ((EditText)findViewById(R.id.username_input)).setError("Username can only contain alphabets or letters.");
            input_val_auth_error = true;
        }
        if(!checkUniqueUsername(userName))
        {
            ((EditText)findViewById(R.id.username_input)).setError("This username is already in use, please enter a different one.");
            input_val_auth_error = true;
        }
        if(!PASSWORD_PATTERN.matcher(userPassword).matches())
        {
            ((EditText)findViewById(R.id.pwd_input)).setError("Password too weak.");
            input_val_auth_error = true;
        }
        if(!userPassword.equals(userCpassword))
        {
            ((EditText)findViewById(R.id.cnfpwd_input)).setError("Passwords do not match.");
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
        intent.putExtra("userName", userName);
        intent.putExtra("OTP", OTP);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userPassword", userPassword);
        startActivity(intent);
    }

}