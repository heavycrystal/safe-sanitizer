package com.example.simpletodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyTODOsActivity extends AppCompatActivity {

    private ListView mainListView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_todos);

        mainListView = (ListView) findViewById(R.id.myTODOListView);
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        loadListView();

        mainListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MyTODOsActivity.this, "Clicked: "+item, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        actionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent addIntent = new Intent("AddTODOActivity");
                        startActivity(addIntent);
                    }
                }
        );

    }

    private void loadListView(){
        adapter  = new CustomAdapter(this, SimpleTODO.GetAllTODOs(this, "Title"));
        mainListView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadListView();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("ListViewDataUpdated"));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }


    private final BroadcastReceiver mMessageReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            loadListView();
        }
    };
}
