package com.example.trial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DBUser extends AppCompatActivity implements AddItemDialog.ItemDialogListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, ArrayList<String>> itemMap;
    private String[] itemArray;
    DBHelper db = new DBHelper(this);
    String username = "user1";
    String category="food";
    public static String NEWCATEGORY=null;

    public String user1 = "user1";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b_user);
        Log.d("dbtrial", "in onCreate");
        createRecyclerView();
    }
    private void createRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        itemArray = db.getItemArray(username, category);
        Log.d("dbtrial", String.valueOf(itemArray.length)+" in createRecyclerView");
        itemMap = db.getItemMap(username, category);
        Log.d("dbtrial", String.valueOf(itemMap.size())+" in createRecyclerView");
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(itemArray, itemMap);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Log.d("dbtrial", "onResume");
        Log.d("dbtrial", "no s");
        //createRecyclerView();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("dbtrial", "onRestart called");
    }
    public void showItemDialog(View view) {
        AddItemDialog newDialog = new AddItemDialog();
        newDialog.show(getSupportFragmentManager(), "dialog");
        Log.d("dbtrial", "inside ShowItemDialog");
    }
    @Override
    public void sendItemName(String itemName, double itemPrice, int itemQty, Uri imageUri) {
        db.addItem(username, category, itemName, itemPrice, itemQty, imageUri.toString());
        Log.d("dbtrial", "added: "+itemName);
        createRecyclerView();
    }
}