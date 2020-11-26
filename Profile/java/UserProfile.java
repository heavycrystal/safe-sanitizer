package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    public static String USER = "user1";
    //use database to fill the TextViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        messageAnimation();

        //onClickListener for custom back button
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void messageAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        TextView tv = (TextView) findViewById(R.id.wlcmMsg);
        tv.setText("Hello "+USER.toUpperCase()+"!");
        tv.startAnimation(a);
    }
    private void shareWithFriends(){}
    private void editUserInfo(){};
    private void logOut(){}
}