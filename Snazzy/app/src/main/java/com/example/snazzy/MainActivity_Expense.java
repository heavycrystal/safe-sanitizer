package com.example.snazzy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity_Expense extends AppCompatActivity {

    CircularProgressButton loadingMe;
    ImageView mainLogo;
    Animation rotate,scale;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_expense);
        if(getIntent().getBooleanExtra("EXIT",false)){
            finish();
        }
        appName = findViewById(R.id.appName);
        mainLogo = findViewById(R.id.mainLogo);

        scale = AnimationUtils.loadAnimation(this,R.anim.scaler);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);

        appName.startAnimation(scale);
        mainLogo.startAnimation(rotate);
        loadingMe = (CircularProgressButton)findViewById(R.id.loadingMe);
        loadingMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, String, String> GetStarted = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if(s.equals("done")){
                            Intent i = new Intent(com.example.snazzy.MainActivity_Expense.this, com.example.snazzy.TripSelection.class);
                            startActivity(i);
                        }
                    }
                };

                loadingMe.startAnimation();
                GetStarted.execute();

            }
        });
    }
}
