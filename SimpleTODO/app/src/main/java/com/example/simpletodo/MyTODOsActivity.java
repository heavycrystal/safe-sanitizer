package com.example.simpletodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                (parent, view, position, id) -> {
                    String item = String.valueOf(parent.getItemAtPosition(position));
                    Toast.makeText(MyTODOsActivity.this, "Clicked: "+item, Toast.LENGTH_SHORT).show();
                }
        );

        actionButton.setOnClickListener(
                v -> {
                    Intent addIntent = new Intent("AddTODOActivity");
                    startActivity(addIntent);
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
