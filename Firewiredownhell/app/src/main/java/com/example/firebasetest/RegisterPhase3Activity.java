package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.util.ui.BucketedTextChangeListener;

import java.util.Objects;
import java.util.Random;

import static com.example.firebasetest.SendgridTestActivity.OTP;
import static com.example.firebasetest.SendgridTestActivity.userEmail;

public class RegisterPhase3Activity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mPhoneTextView;
    private TextView mResendCodeTextView;
    private TextView mCountDownTextView;
    private SpacedEditText mConfirmationCodeEditText;
    View.OnClickListener resend_email_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase3);

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
                countdown();
            }
        };

        countdown();
    }

    protected void verifyCode() {
        if(mConfirmationCodeEditText.getUnspacedText().toString().equals(String.valueOf(OTP))) {
            Intent intent = new Intent(this, RegisterPhase1Activity.class);
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