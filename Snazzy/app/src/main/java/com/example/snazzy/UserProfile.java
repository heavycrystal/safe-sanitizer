package com.example.snazzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    public static String USERNAME = MainActivity.USERNAME;
    public static String PROFESSION = MainActivity.PROFESSION;
    DBHelper db;
    //Uri tediumLogoUri = Uri.parse("android.resource://com.example.snazzy/drawable/tedium_logo_square");
    TextView username, profession, email, phone;
    //use database to fill the TextViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        messageAnimation();

        db = new DBHelper(this);
        username = findViewById(R.id.userName);
        profession = findViewById(R.id.profession);
        email = findViewById(R.id.emailID);
        phone = findViewById(R.id.phoneNumber);

        username.setText(USERNAME);
        profession.setText(PROFESSION);
        ArrayList<String> data = db.getUserData(USERNAME);
        email.setText("Email: "+data.get(0));
        phone.setText("Phone: "+data.get(1));

        //onClickListener for custom back button
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        Button share = findViewById(R.id.share);
        share.setOnClickListener(v -> shareWithFriends());

        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }
    private void messageAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        TextView tv = (TextView) findViewById(R.id.wlcmMsg);
        tv.setText("Hello "+USERNAME.toUpperCase()+"!");
        tv.startAnimation(a);
    }
    private void shareWithFriends(){
        String message = "Check out Tedium - A cool Lifestyle app here\n"
                +"https://github.com/heavycrystal/safe-sanitizer";

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");

        //File file = new File(tediumLogoUri.getPath());
        //Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".provider", file);
        //intent.putExtra(Intent.EXTRA_STREAM,photoURI);
        //intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }
    private void logOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        MainActivity.USERNAME=null;
        MainActivity.PROFESSION=null;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}