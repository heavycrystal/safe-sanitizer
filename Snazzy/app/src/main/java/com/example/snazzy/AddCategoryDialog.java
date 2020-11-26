package com.example.snazzy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddCategoryDialog extends AppCompatDialogFragment {
    private CatDialogListener listener;
    private EditText nameInput;
    private Button uploadBtn;
    private TextView imageName;
    private String categoryName=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category, null);

        nameInput = view.findViewById(R.id.editItemName);

        builder.setView(view)
                .setTitle("Add Category")
                // Add action buttons
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        categoryName = nameInput.getText().toString();
                        if(categoryName!=null) {
                            listener.sendCatName(categoryName);
                        }
                        else
                            Toast.makeText(getContext(), "Enter the category name", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CatDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement CatDialogListener");
        }
    }
    //interface for returning values to previous activity
    public interface CatDialogListener{
        void sendCatName(String categoryName);
    }
}
