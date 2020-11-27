package com.example.snazzy;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

public class AboutUs extends AppCompatActivity {

    ImageView sun, day_landscape, night_landscape;
    View daysky, nightsky;
    DayNightSwitch dayNightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        sun=findViewById(R.id.sun);
        day_landscape=findViewById(R.id.day_landscape);
        night_landscape=findViewById(R.id.night_landscape);
        daysky=findViewById(R.id.day_bg);
        nightsky=findViewById(R.id.night_bg);
        dayNightSwitch=findViewById(R.id.day_night_switch);

        sun.setTranslationY(-110);

        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if(is_night){
                    sun.animate().translationY(110).setDuration(1000);
                    day_landscape.animate().alpha(0).setDuration(1300);
                    daysky.animate().alpha(0).setDuration(1300);
                }
                else{
                    sun.animate().translationY(-110).setDuration(1000);
                    day_landscape.animate().alpha(1).setDuration(1300);
                    daysky.animate().alpha(1).setDuration(1300);
                }
            }
        });
    }
}