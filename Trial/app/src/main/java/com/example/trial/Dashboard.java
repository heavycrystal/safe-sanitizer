package com.example.trial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.GridLayout.*;

public class Dashboard extends AppCompatActivity implements AddCategoryDialog.CatDialogListener{
    DBHelper mydb = new DBHelper(this);

    int width=1080;
    Intent intent;
    Uri imageUri, plain=null;
    ArrayList<String> catList;

    public String username = "user1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        createDashboard();

    }
    private void createDashboard(){
        int r=0, c=0;
        GridLayout gl = findViewById(R.id.gl);
        gl.removeAllViews();
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
            ImageButton btn = new ImageButton(getApplicationContext());
            Button overlayBtn = new Button(getApplicationContext());
            btn.setTag(cat);
            overlayBtn.setTag(cat);
            overlayBtn.setText(cat.toUpperCase());
            overlayBtn.setTextSize(24);

            overlayBtn.setTextColor(getResources().getColor(R.color.white));
            GridLayout.Spec rowSpan = GridLayout.spec(r, 1);
            GridLayout.Spec colSpan = GridLayout.spec(c, 1);
            GridLayout.LayoutParams gp = new GridLayout.LayoutParams(rowSpan, colSpan);
            gp.setGravity(Gravity.LEFT | Gravity.TOP);
            gl.addView(btn, gp);
            gl.addView(overlayBtn, gp);

            btn.setMinimumWidth(width / 2);
            btn.setMinimumHeight(width / 2);
            overlayBtn.setWidth(width / 2);
            overlayBtn.setHeight(width / 2);
            overlayBtn.setElevation(50);
            overlayBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btn.setPadding(10, 10, 10, 10);
            imageUri = Uri.parse(mydb.getCategoryImage(username, cat));
            Glide.with(getApplicationContext()).load(imageUri).centerCrop().into(btn);
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(80);
            btn.setBackgroundDrawable(shape);
            //btn.setClipToOutline(true);
            Log.d("here", "before onclick");
            overlayBtn.setOnClickListener(new View.OnClickListener() {
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
        createDashboard();
        //Refresh your stuff here
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
    public void sendCatName(String categoryName, Uri image) {
        mydb.addCategory(username, categoryName, image.toString());
        //add toast
        createDashboard();
    }
}