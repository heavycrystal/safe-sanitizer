package com.example.snazzy;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class RegisterPhase3Activity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mPhoneTextView;
    private TextView mResendCodeTextView;
    private TextView mCountDownTextView;
    private SpacedEditText mConfirmationCodeEditText;
    View.OnClickListener resend_email_otp;

    private String userName;
    private int OTP;
    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase3);

        userName = getIntent().getStringExtra("userName");
        OTP = getIntent().getIntExtra("OTP", 123456);
        userEmail = getIntent().getStringExtra("userEmail");
        userPassword = getIntent().getStringExtra("userPassword");

        mProgressBar = findViewById(R.id.top_progress_bar);
        mPhoneTextView = findViewById(R.id.edit_phone_number);
        mCountDownTextView = findViewById(R.id.ticker);
        mResendCodeTextView = findViewById(R.id.resend_code);
        mConfirmationCodeEditText = findViewById(R.id.confirmation_code);
        mProgressBar.setVisibility(View.VISIBLE);

        mConfirmationCodeEditText.setText("------");
        mConfirmationCodeEditText.addTextChangedListener(new LocalBucketedChangeListener(
                mConfirmationCodeEditText, 6, "-",
                new LocalBucketedChangeListener.ContentChangeCallback() {
                    @Override
                    public void whenComplete() {
                        verifyCode();
                    }

                    @Override
                    public void whileIncomplete() {

                    }
                }));

        resend_email_otp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                OTP = 100000 + rand.nextInt(900000);
                SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        SendEmailService.getInstance(getApplicationContext()).SendOTPEmail(userEmail, OTP);
                    }
                });
                ((TextView)findViewById(R.id.resend_code)).setOnClickListener(null);
                ((TextView)findViewById(R.id.resend_code)).setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.ticker)).setVisibility(View.VISIBLE);
                countdown();
            }
        };

        countdown();
    }

    protected void verifyCode() {
        if(mConfirmationCodeEditText.getUnspacedText().toString().equals(String.valueOf(OTP))) {
            Intent intent = new Intent(this, RegisterPhase2Activity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("OTP", OTP);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }
        else {
            mConfirmationCodeEditText.setError("Verification code does not match.");
        }
    }

    protected void countdown() {
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.ticker)).setText("Resend available in " + millisUntilFinished / 1000 + " seconds.");
            }

            public void onFinish() {
                ((TextView)findViewById(R.id.ticker)).setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.resend_code)).setText("Resend OTP.");
                ((TextView)findViewById(R.id.resend_code)).setOnClickListener(resend_email_otp);
                ((TextView)findViewById(R.id.resend_code)).setVisibility(View.VISIBLE);
            }
        }.start();
    }

}