package com.example.snazzy;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements com.example.snazzy.AddCategoryDialog.CatDialogListener, ShareGroceryDialog.SharedGroceryListener{
    DBHelper mydb = new DBHelper(this);
    View view;
    Button btn;
    Intent intent;

    int width=1080;
    private String DELCAT;
    Uri imageUri, plain=null;
    ArrayList<String> catList;

    public String username = "user1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        createDashboard();
        intent = getIntent();
        DELCAT = intent.getStringExtra("DELCAT");
        if(DELCAT!=null)
            Toast.makeText(this, "Deleted category "+DELCAT, Toast.LENGTH_SHORT).show();

    }
    private void createDashboard(){
        int r=0, c=0;
        GridLayout gl = findViewById(R.id.gl);
        gl.removeAllViews();
        DisplayMetrics displayMetrics = getApplication().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.d("dimens", String.valueOf(dpHeight)+" "+String.valueOf(dpWidth));
        catList=mydb.getCategories(username);
        //Adding FloatingActionButton onClick
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatDialog(view);
            }
        });
        for(String cat: catList) {

            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.category_view, null);
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
            view.setPadding(40,0,40,70);
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
    public void openShareDialog(View view){
        ShareGroceryDialog dialog = new ShareGroceryDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    public void goToCategory(View view){
        intent = new Intent(this, com.example.snazzy.ViewCategory.class);
        intent.putExtra(com.example.snazzy.MainActivity_Ankita.MESSAGE, (String)view.getTag());
        startActivity(intent);
    }
    public void showCatDialog(View view) {
        com.example.snazzy.AddCategoryDialog newDialog = new com.example.snazzy.AddCategoryDialog();
        newDialog.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void sendCatName(String categoryName) {
        mydb.addCategory(username, categoryName);
        //add toast
        createDashboard();
    }
    @Override
    public void sendConfirmation(boolean shared){
        if(shared==true)
            Toast.makeText(getApplicationContext(), "Shared!", Toast.LENGTH_SHORT);
    }
}