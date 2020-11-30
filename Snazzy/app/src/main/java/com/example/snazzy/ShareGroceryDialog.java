package com.example.snazzy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ShareGroceryDialog extends AppCompatDialogFragment {
    private com.example.snazzy.DBHelper db;
    private SharedGroceryListener listener;
    private static String USERNAME = Dashboard.USERNAME;
    private Button whatsapp, email;
    private int totalCost=0;
    private ArrayList<String> groceryList = new ArrayList<String>();
    private String[] itemArray;
    private ArrayList<String> catList;
    private HashMap<String, ArrayList<String>> itemMap;
    String unit, message="";
    int price, qty;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sharing_options, null);

        db = new com.example.snazzy.DBHelper(getContext());
        catList = db.getCategories(USERNAME);
        Log.d("DEBUG", catList.size()+"");
        //create the grocery list
        for(String cat: catList){
            itemArray = db.getItemArray(USERNAME, cat);
            itemMap = db.getItemMap(USERNAME, cat);
            for(String item: itemArray){
                price = Integer.parseInt(itemMap.get(item).get(0));
                qty = Integer.parseInt(itemMap.get(item).get(1));
                unit = itemMap.get(item).get(2);
                groceryList.add(item+ " - "+qty+" "+unit);
                totalCost+=qty*price;
            }
        }
        Log.d("DEBUG", groceryList.size()+"");
        Log.d("DEBUG", totalCost+"");

        whatsapp = view.findViewById(R.id.whatsApp);
        whatsapp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareViaWhatsapp();
            }
        });

        //onClickListener for sharing via e-mail
        email = view.findViewById(R.id.eMail);
        email.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareViaEmail();
            }
        });

        //build the dialog
        builder.setView(view)
                .setTitle("Share via")
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SharedGroceryListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement CatDialogListener");
        }
    }
    //interface for returning values to previous activity
    public interface SharedGroceryListener{
        void sendConfirmation(boolean shared);
    }
    private void shareViaWhatsapp(){
        message="";
        for(String line: groceryList){
            message = message + line + "\n";
        }
        message = message+"Total Cost "+totalCost;
        Log.d("DEBUG", message);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }
    private void shareViaEmail(){
        String userEmail = db.getEmail(USERNAME);
        SendEmailService.getInstance(getContext()).emailExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SendEmailService.getInstance(getContext()).SendGroceryListMail(userEmail, USERNAME, groceryList, null, getContext(), System.currentTimeMillis()/1000);
                Toast.makeText(getContext(), "Email sent to: "+userEmail, Toast.LENGTH_LONG).show();

            }
        });

    }
}
