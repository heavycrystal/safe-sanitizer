package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;
import com.bumptech.glide.*;

import java.io.File;
import java.io.IOException;
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


           /* Uri plain = null;
            try
            {
                File temp_file = File.createTempFile("lololol", ".jpg");
                Log.e("Epp", temp_file.canRead() + " " + temp_file.canWrite() + " " + temp_file.canExecute());
                plain = Uri.fromFile(temp_file);
            }
            catch(Exception e)
            {

            } */
            Log.d("dj", filename.toString());
           /* UCrop.of(filename, plain)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(320, 320)
                    .start(this);

            Log.d("dj", "after UCrop"); */
            ((TextView)findViewById(R.id.textView9)).setText(TensorflowTest.infer_image(filename, this));

            /*SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    SendEmailService.getInstance(getApplicationContext()).SendReminderMail("kevinkbiju@gmail.com", filename, ProfileActivity.this,  (System.currentTimeMillis() / 1000L) + 21600);
                }
            });*/
        }

        if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(resultData);
            Log.d("Epp", resultUri.toString());

            //butn is button duh
            ((ImageButton)findViewById(R.id.imageButton)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            String imageURL = resultUri.toString();
            Glide.with(getApplicationContext()).load(imageURL).centerCrop().into((ImageButton)findViewById(R.id.imageButton));
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(80);
            ((ImageButton)findViewById(R.id.imageButton)).setBackgroundDrawable(shape);
            ((ImageButton)findViewById(R.id.imageButton)).setClipToOutline(true);
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
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE);

    }


}