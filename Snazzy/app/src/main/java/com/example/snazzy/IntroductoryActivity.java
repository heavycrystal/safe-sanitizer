package com.example.snazzy;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView logo,appName,splashImg;
    LottieAnimationView lottieAnimationView;
    private static final int NUM_PAGES=3;

    Animation anim;
    private static final int SPLASH_TIME_OUT =5500;
    SharedPreferences msharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        setContentView(R.layout.activity_introductory);

        logo=findViewById(R.id.logo);
        appName=findViewById(R.id.app_name);
        splashImg=findViewById(R.id.img);
        lottieAnimationView=findViewById(R.id.splash);

        ViewPager viewPager = findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        anim= AnimationUtils.loadAnimation(this, R.anim.o_b_anim);
        viewPager.startAnimation(anim);

        splashImg.animate().translationY(-2500).setDuration(1500).setStartDelay(4000);
        logo.animate().translationY(1400).setDuration(1500).setStartDelay(4000);
        appName.animate().translationY(1400).setDuration(1500).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(1500).setStartDelay(4000);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            msharedPref=getSharedPreferences("SharedPrefs", MODE_PRIVATE);
            boolean isFirstTime=msharedPref.getBoolean("firstTime", true);
            if(isFirstTime){
                SharedPreferences.Editor editor=msharedPref.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();
            }
            else{
                //Kevin, this one's for you - add the login activity ka code here. Basically, make this
                //an intent to go to the login activity after it shows the OnBoard for the first time
                //Basically, here - just put Intent intent to make it go to login screen.
            }
        }, SPLASH_TIME_OUT);
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new OnBoardingFragment1();
                case 1:
                    return new OnBoardingFragment2();
                case 2:
                    return new OnBoardingFragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}