package com.example.snazzy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class SearchDialog extends AppCompatDialogFragment {
    private SearchView search;
    public LinearLayout ll;

    DBHelper db;
    String USERNAME = MainActivity.USERNAME;
    String PROFESSION = MainActivity.PROFESSION;
    private ArrayList<String> catResults, itemResults;
    private HashMap<String, String> itemMaps;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_dialog_layout, null);

        db = new DBHelper(getContext());
        ll = view.findViewById(R.id.linear);

        search = view.findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewResults(query, ll);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null && !newText.equals(""))
                    viewResults(newText, ll);
                else
                    ll.removeAllViews();
                return false;
            }
        });

        builder.setView(view)
                .setTitle("Search")
                // Add action buttons
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void viewResults(String query, LinearLayout ll){

        catResults = db.getAlikeCategories(USERNAME, query);
        Log.d("CATS", String.valueOf(catResults.size()));
        Log.d("CHECKING", String.valueOf(query.length()));

        itemResults = db.getAlikeItemList(USERNAME, query);
        itemMaps = db.getAlikeItemMap(USERNAME, query);
        Log.d("CHECKING Items", String.valueOf(itemResults.size()));

        ll.removeAllViews();

        View v;
        for (String cat : catResults) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, null);
            ((Button) v.findViewById(R.id.item)).setText(cat);
            v.findViewById(R.id.item).setTag(cat);
            v.setPadding(0, 5, 0, 5);
            v.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("HUH", "inside onClick");
                    Intent intent = new Intent(getContext(), ViewCategory.class);
                    intent.putExtra("MESSAGE", (String)view.getTag());
                    startActivity(intent);
                }
            });
            ll.addView(v);
        }
        for (String item : itemResults) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, null);
            ((Button) v.findViewById(R.id.item)).setText(item);
            v.findViewById(R.id.item).setTag(item);
            ((TextView)v.findViewById(R.id.itemInfo)).setText("in "+itemMaps.get(item));
            ll.addView(v);
            v.setPadding(0, 5, 0, 5);
            v.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ViewCategory.class);
                    intent.putExtra("MESSAGE", itemMaps.get(item));
                    intent.putExtra("ITEM", item);
                    startActivity(intent);
                }
            });
        }


    }
}
