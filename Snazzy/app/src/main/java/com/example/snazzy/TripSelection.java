package com.example.snazzy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;


public class TripSelection extends AppCompatActivity {

    private TextView tripNameTextView;
    private String TripName;
    com.example.snazzy.DatabaseHelper myDb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      For Transparent Status bar uncomment below line
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_trip_selection);

        myDb = new com.example.snazzy.DatabaseHelper(this);
        cursor = myDb.getAllData();
        if(cursor.moveToFirst()){
            String tripName = cursor.getString(2);
            Intent i = new Intent(com.example.snazzy.TripSelection.this, com.example.snazzy.Home.class);
            startActivity(i);
            return;
        }
        final Button addTripBtn = findViewById(R.id.addTrip);
        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripNameTextView = findViewById(R.id.tripName);
                TextView warningTextView = findViewById(R.id.warningText);
                if(tripNameTextView.getText().length()==0){
                    warningTextView.setText("Please Enter a valid name of Trip");
                    TastyToast.makeText(com.example.snazzy.TripSelection.this,"Oops..", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }else{
                    warningTextView.setText("");
                    TripName = tripNameTextView.getText().toString();
                    TastyToast.makeText(com.example.snazzy.TripSelection.this,"Trip Selected", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent i = new Intent(com.example.snazzy.TripSelection.this, com.example.snazzy.GroupCreation.class);
                    i.putExtra("TripName",TripName);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
