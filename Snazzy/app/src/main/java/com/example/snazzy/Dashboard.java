package com.example.snazzy;
//Ankita's Dashboard

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements AddCategoryDialog.CatDialogListener, ShareGroceryDialog.SharedGroceryListener{
    DBHelper mydb = new DBHelper(this);
    View view;
    Button btn;
    Intent intent;
    FloatingActionButton dashSearch;
    public static String USERNAME;
    public static String PROFESSION;

    int width=1080;
    private String DELCAT;
    ArrayList<String> catList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USERNAME = MainActivity.USERNAME;
        Log.d("USERNAME", USERNAME);
        PROFESSION = MainActivity.PROFESSION;
        Log.d("PROFESSION", PROFESSION);

        if(PROFESSION==null)
            PROFESSION="Homemaker";
        else if(PROFESSION.equals("Student")) {
            setContentView(R.layout.student_dashboard_activity);
            if(mydb.getCategories(USERNAME).size()==0){
                mydb.addCategory(USERNAME, "Stationery");
                mydb.addCategory(USERNAME, "Snacks");
                mydb.addCategory(USERNAME, "Books");
            }
        }
        else if(PROFESSION.equals("Professional")) {
            setContentView(R.layout.professional_dashboard_activity);
            if(mydb.getCategories(USERNAME).size()==0){
                mydb.addCategory(USERNAME, "Office Supplies");
                mydb.addCategory(USERNAME, "Medicines");
                mydb.addCategory(USERNAME, "Bills");
                mydb.addCategory(USERNAME, "Groceries");
            }
        }
        else if(PROFESSION.equals("Homemaker")) {
            setContentView(R.layout.homemaker_dashboard_activity);
            if(mydb.getCategories(USERNAME).size()==0){
                mydb.addCategory(USERNAME, "Groceries");
                mydb.addCategory(USERNAME, "Medicines");
                mydb.addCategory(USERNAME, "Bills");
                mydb.addCategory(USERNAME, "Childcare");
            }
        }

        createDashboard();

        intent = getIntent();
        DELCAT = intent.getStringExtra("DELCAT");
        if(DELCAT!=null)
            Toast.makeText(this, "Deleted category "+DELCAT, Toast.LENGTH_SHORT).show();

        //Adding FloatingActionButton onClick
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatDialog(view);
            }
        });

        dashSearch = findViewById(R.id.dashSearch);
        dashSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialog(view);
            }
        });

    }
    private void createDashboard(){
        int r=0, c=0;
        GridLayout gl = findViewById(R.id.gl);
        gl.removeAllViews();
        catList=mydb.getCategories(USERNAME);

        for(String cat: catList) {

            if(PROFESSION.equals("Student"))
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.student_category_layout, null);
            else if(PROFESSION.equals("Professional"))
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.professional_category_layout, null);
            else if(PROFESSION.equals("Homemaker"))
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.homemaker_category_layout, null);

            btn = view.findViewById(R.id.buttonName);
            btn.setText(cat);
            btn.setTag(cat);
            GridLayout.Spec rowSpan = GridLayout.spec(r, 1);
            GridLayout.Spec colSpan = GridLayout.spec(c, 1);
            GridLayout.LayoutParams gp = new GridLayout.LayoutParams(rowSpan, colSpan);
            gp.setGravity(Gravity.TOP);
            gl.addView(view, gp);
            btn.setMinimumWidth(width / 2);
            btn.setMinimumHeight(width / 2);
            final Typeface face = ResourcesCompat.getFont(getApplicationContext(), R.font.viga);
            btn.setTypeface(face);
            view.setPadding(35,0,35,70);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToCategory(view);
                }
            });
            ++c;
            if (c == 2) {
                c = 0;
                ++r;
            }
        }
    }
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Log.d("inside", "onResume");
        createDashboard();
        //Refresh your stuff here
    }
    public void showSearchDialog(View view){
        SearchDialog dialog = new SearchDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    public void openShareDialog(View view){
        ShareGroceryDialog dialog = new ShareGroceryDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    public void goToCategory(View view){
        intent = new Intent(this, ViewCategory.class);
        intent.putExtra(MainActivity.MESSAGE, (String)view.getTag());
        startActivity(intent);
    }
    public void showCatDialog(View view) {
        AddCategoryDialog newDialog = new AddCategoryDialog();
        newDialog.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void sendCatName(String categoryName) {
        mydb.addCategory(USERNAME, categoryName);
        //add toast
        createDashboard();
    }
    @Override
    public void sendConfirmation(boolean shared){
        if(shared==true)
            Toast.makeText(getApplicationContext(), "Shared!", Toast.LENGTH_SHORT).show();
    }
    public void setUserAndProfession(){
        mydb = new DBHelper(getApplicationContext());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            Log.e("EMAIL", userEmail);
            mydb = new DBHelper(getApplicationContext());
            USERNAME = mydb.getUsername(userEmail);
            PROFESSION = mydb.getProf(USERNAME);
           Log.d("SET", "user and profession");
        }
    }
}