package com.example.snazzy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewCategory extends AppCompatActivity implements AddItemDialog.ItemDialogListener, ViewItemDialog.ViewItemDialogListener, DelCatDialog.DelListener{
    static Intent intent;
    DBHelper db;
    LinearLayout ll;
    View v;
    public static String USERNAME = MainActivity_Ankita.USER;
    public static String CATEGORY;
    public static String SELECTED_ITEM=null;
    public String ADD_SUCCESS, EDIT_SUCCESS, DEL_SUCCESS;
    String itemArray[];
    HashMap<String, ArrayList<String>> itemMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        intent = getIntent();
        CATEGORY = intent.getStringExtra(MainActivity_Ankita.MESSAGE);
        TextView catName = findViewById(R.id.catName);
        catName.setText(CATEGORY.toUpperCase());
        db = new DBHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setTag(CATEGORY);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog(view);
            }
        });

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
            v.findViewById(R.id.item).setTag(item);
            ((TextView)v.findViewById(R.id.priceView)).setText("Rs. "+itemMap.get(item).get(0));
            ((TextView)v.findViewById(R.id.qtyView)).setText(itemMap.get(item).get(1)+" "+itemMap.get(item).get(2));
            ll.addView(v);
            v.setPadding(50,0,50,50);

            ((Button)v.findViewById(R.id.item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SELECTED_ITEM = view.getTag().toString();
                    viewItemDialog(view);
                }
            });
        }
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("dbtrial", "onRestart called");
    }
    public void viewItemDialog(View v){
        ViewItemDialog.selectedItem(USERNAME, CATEGORY, v.getTag().toString());
        ViewItemDialog newDialog = new ViewItemDialog();
        newDialog.show(getSupportFragmentManager(), "dialog");
        Log.d("dbtrial", "inside ShowItemDialog");
    }
    public void showItemDialog(View view) {
        ViewItemDialog.selectedItem(USERNAME, CATEGORY, view.getTag().toString());
        AddItemDialog newDialog = new AddItemDialog();
        newDialog.show(getSupportFragmentManager(), "dialog");
    }
    public void showConfirmationDialog(View view){
        DelCatDialog dialog = new DelCatDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void sendItemName(String itemName, double itemPrice, int itemQty, String unitName, Uri imageUri) {
        boolean added = db.addItem(USERNAME, CATEGORY, itemName, itemPrice, itemQty, unitName, imageUri.toString());
        Log.d("dbtrial", "added: "+itemName);
        ADD_SUCCESS = itemName;
        createList();
        if(ADD_SUCCESS!=null && added==true)
            Toast.makeText(this, "Item '"+ADD_SUCCESS+"' added successfully", Toast.LENGTH_LONG).show();
        else if(added==false)
            Toast.makeText(this, "Item '"+ADD_SUCCESS+"' already exists", Toast.LENGTH_LONG).show();

    }
    @Override
    public void sendEditedItemName(String itemName, double itemPrice, int itemQty, String unitName, Uri imageUri){
        db = new DBHelper(this);
        EDIT_SUCCESS = itemName;
        boolean edited = db.editItem(USERNAME, CATEGORY, SELECTED_ITEM, itemName, itemPrice, itemQty, unitName, imageUri.toString());
        createList();
        if(EDIT_SUCCESS!=null && edited==true)
            Toast.makeText(this, "Item '"+EDIT_SUCCESS+"' edited successfully", Toast.LENGTH_LONG).show();
        else if(edited==false)
            Toast.makeText(this, "Item '"+EDIT_SUCCESS+"' already exists", Toast.LENGTH_LONG).show();
    }
    @Override
    public void sendDeletedItemName(String itemName){
        DEL_SUCCESS = itemName;
        db = new DBHelper(this);
        boolean deleted = db.deleteItem(USERNAME, CATEGORY, itemName);
        createList();
        if(DEL_SUCCESS!=null && deleted==true)
            Toast.makeText(this, "Item '"+DEL_SUCCESS+"' deleted successfully", Toast.LENGTH_LONG).show();
    }
    @Override
    public void sendDelCatName(String categoryName, boolean confirm){
        if(confirm==true){
            db.deleteCategory(USERNAME, CATEGORY);
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.putExtra("DELCAT", categoryName);
            startActivity(intent);
        }

    }
}