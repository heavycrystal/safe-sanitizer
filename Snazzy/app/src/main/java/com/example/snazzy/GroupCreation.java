package com.example.snazzy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GroupCreation extends AppCompatActivity {

    String TripName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        TripName = getIntent().getStringExtra("TripName");
        TextView tripNameHeader = findViewById(R.id.tripNameHeader);
        tripNameHeader.setText("Trip: "+ TripName);

        Button sizeSelectionBtn = findViewById(R.id.sizeSelectionBtn);
        sizeSelectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView grps = findViewById(R.id.groupSize);
                TextView warningText = findViewById(R.id.warningText);
                try{
                    Integer GrpSize = Integer.parseInt(grps.getText().toString());
                    if(GrpSize<=1){
                        warningText.setText("Please enter a valid group size");
                    }else{
                        warningText.setText("");
                        Intent i = new Intent(com.example.snazzy.GroupCreation.this,AddMembers.class);
                        i.putExtra("TripName",TripName);
                        i.putExtra("GroupSize",GrpSize.toString());
                        startActivity(i);
                    }
                }catch (Exception e){
                    warningText.setText("Please enter a valid group size");
                }

            }
        });
    }
}
