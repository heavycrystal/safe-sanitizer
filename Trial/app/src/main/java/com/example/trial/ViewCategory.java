package com.example.trial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.trial.data.ViewItemDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ViewCategory extends AppCompatActivity implements AddItemDialog.ItemDialogListener, ViewItemDialog.ViewItemDialogListener{
    static Intent intent;
    DBHelper db = new DBHelper(this);
    ValueEventListener listener;
    LinearLayout ll;
    View v;
    public static String USERNAME = "user1";
    public String CATEGORY;
    public String ADD_SUCCESS;
    String itemArray[];
    HashMap<String, ArrayList<String>> itemMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        intent = getIntent();
        CATEGORY = intent.getStringExtra(MainActivity.MESSAGE);
        TextView catName = findViewById(R.id.catName);
        catName.setText(CATEGORY.toUpperCase());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setTag(CATEGORY);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog(view);
            }
        });

        //Database operations
        Log.d("dbtrial", "in oncreate");
        //createRecyclerView();
        createList();


    }
    private void createList(){
        ll = findViewById(R.id.ll);
        ll.removeAllViews();

        itemArray = db.getItemArray(USERNAME, CATEGORY);
        itemMap = db.getItemMap(USERNAME, CATEGORY);
        for(String item: itemArray){
            v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_text_view, null);

            ((Button)v.findViewById(R.id.item)).setText(item);
            ((Button)v.findViewById(R.id.item)).setTag(item);
            ((TextView)v.findViewById(R.id.priceView)).setText("Price: "+itemMap.get(item).get(0));
            ((TextView)v.findViewById(R.id.qtyView)).setText("Qty: "+itemMap.get(item).get(1));
            ll.addView(v);
        }
    }
    private void createRecyclerView(){
        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        HashMap<String, ArrayList<String>> itemMap=null;
        String[] itemArray=null;
        recyclerView = findViewById(R.id.recyclerView);
        itemArray = db.getItemArray(USERNAME, CATEGORY);
        itemMap = db.getItemMap(USERNAME, CATEGORY);
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
        boolean added = db.addItem(USERNAME, CATEGORY, itemName, itemPrice, itemQty, imageUri.toString());
        Log.d("dbtrial", "added: "+itemName);
        ADD_SUCCESS = itemName;
        createList();
        if(ADD_SUCCESS!=null && added==true)
            Toast.makeText(this, "Item '"+ADD_SUCCESS+"' added successfully", Toast.LENGTH_LONG).show();
        else if(added==false)
            Toast.makeText(this, "Item '"+ADD_SUCCESS+"' already exists", Toast.LENGTH_LONG).show();

    }
    public void deleteCategory(View view){
        db.deleteCategory(USERNAME, CATEGORY);
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);

    }
    @Override
    public void sendItemName(String itemName) {
        Toast.makeText(this, "Successfully edited item "+itemName, Toast.LENGTH_LONG);
    }
}