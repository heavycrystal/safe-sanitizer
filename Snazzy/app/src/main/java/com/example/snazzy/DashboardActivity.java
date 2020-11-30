package com.example.snazzy;
//Sanket's Dashboard

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;
    ImageButton todobutton;
    ImageButton dashboardbutton;
    ImageButton reminderbutton;
    ImageButton expensebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sanket);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        todobutton = findViewById(R.id.TodoButton);
        imageView = findViewById(R.id.thumbnail_image_header);
        dashboardbutton = findViewById(R.id.dashboard_anki);
        reminderbutton = findViewById(R.id.reminderbtn);
        expensebutton = findViewById(R.id.expensebtn_sank);

        reminderbutton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AlarmActivity.class);//Add ankita's dashboard here
            startActivity(intent);
        });

        todobutton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity_Todo.class);
            startActivity(intent);
        });

        dashboardbutton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, Dashboard.class);//Add ankita's dashboard here
            startActivity(intent);
        });

        expensebutton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity_Expense.class);//Add ankita's dashboard here
            startActivity(intent);
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){

                case R.id.nav_home:
                    intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    break;

                case R.id.nav_about:
                    intent = new Intent(DashboardActivity.this, AboutUs.class);
                    startActivity(intent);
                    break;

                case R.id.nav_dash:
                    intent = new Intent(DashboardActivity.this, Dashboard.class);
                    startActivity(intent);
                    break;

                case R.id.nav_reminder:
                    intent = new Intent(DashboardActivity.this, AlarmActivity.class);
                    startActivity(intent);
                    break;

                case R.id.nav_ToDo:
                    intent = new Intent(DashboardActivity.this, MainActivity_Todo.class);
                    startActivity(intent);
                    break;

                case R.id.nav_share:
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareBody = "Check out Tedium - A cool Lifestyle app here https://github.com/heavycrystal/safe-sanitizer";
                    String shareSub = "Check out Tedium - A cool Lifestyle app";
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent, "Share Using"));
                    break;

                case R.id.nav_profile:
                    intent = new Intent(DashboardActivity.this, UserProfile.class);
                    startActivity(intent);
                    break;

                case R.id.nav_Expense:
                    intent = new Intent(DashboardActivity.this, MainActivity_Expense.class);
                    startActivity(intent);
                    break;

            }
            return false;
        });
    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}