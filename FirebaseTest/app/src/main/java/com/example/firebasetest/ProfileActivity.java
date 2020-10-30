package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static com.example.firebasetest.SendgridTestActivity.OTP;
import static com.example.firebasetest.SendgridTestActivity.userEmail;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Uri filename;

    private static final int PICK_FILE = 17;

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_FILE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            filename = null;
            if (resultData != null) {
                filename = resultData.getData();
            }

            SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    SendEmailService.getInstance(getApplicationContext()).SendReminderMail("kevinkbiju@gmail.com", filename, ProfileActivity.this,  (System.currentTimeMillis() / 1000L) + 300);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.e("P4", String.valueOf((firebaseUser == null)));
        ((TextView)findViewById(R.id.email)).setText(Objects.requireNonNull(firebaseUser).getEmail());
        ((TextView)findViewById(R.id.UID)).setText(Objects.requireNonNull(firebaseUser).getUid());
        ((TextView)findViewById(R.id.textView9)).setText("die");

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);

    }


}