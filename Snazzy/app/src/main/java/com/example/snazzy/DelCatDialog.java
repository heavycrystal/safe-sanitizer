package com.example.snazzy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DelCatDialog extends AppCompatDialogFragment {
    private DelListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);

        builder.setTitle("Delete Category")
                .setMessage("Delete category "+ com.example.snazzy.ViewCategory.CATEGORY+"?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.sendDelCatName(com.example.snazzy.ViewCategory.CATEGORY, true);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.sendDelCatName(com.example.snazzy.ViewCategory.CATEGORY, false);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DelListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement DelDialogListener");
        }
    }
    //interface for returning values to previous activity
    public interface DelListener{
        void sendDelCatName(String categoryName, boolean confirm);
    }
}
