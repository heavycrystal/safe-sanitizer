package com.example.snazzy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

public class AddMembers extends AppCompatActivity{

    com.example.snazzy.DatabaseHelper myDb;
    String TripName;
    String GroupSize;
    int Size;

    public void AddingMember(int i){
        final Button addMember = findViewById(R.id.addMember);
        if(i==Size){
            addMember.setText("Submit");
        }
        if(i>Size){
            return;
        }
        final int id = i;
        final TextView memberName = findViewById(R.id.memberName);
        memberName.setHint(id+". Member Name");
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberName.getText().length()==0) {
                    TextView warningText = findViewById(R.id.warningText);
                    warningText.setText("Please enter a valid member name");
                    return;
                }
                boolean isInserted = myDb.insertDataTb1(memberName.getText().toString(),TripName);
                if(isInserted)
                    TastyToast.makeText(com.example.snazzy.AddMembers.this,"Member Added", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                else
                    TastyToast.makeText(com.example.snazzy.AddMembers.this,"Member Not Added", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                System.out.println(memberName.getText().toString()+id);
                if(addMember.getText().equals("Submit")){
                    Intent intent = new Intent(com.example.snazzy.AddMembers.this, com.example.snazzy.Home.class);
                    startActivity(intent);
                }
                memberName.setText("");
                AddingMember(id+1);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        myDb = new com.example.snazzy.DatabaseHelper(this);
        TripName = getIntent().getStringExtra("TripName");
        GroupSize = getIntent().getStringExtra("GroupSize");
        Size = Integer.parseInt(GroupSize);
        AddingMember(1);
    }
}
