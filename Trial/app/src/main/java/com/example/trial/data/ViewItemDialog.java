package com.example.trial.data;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.trial.AddItemDialog;
import com.example.trial.DBHelper;
import com.example.trial.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ViewItemDialog extends AppCompatDialogFragment {
    DBHelper db = new DBHelper(getContext());
    private static final int PICK_FILE = 14;
    private AddItemDialog.ItemDialogListener listener;
    private EditText nameInput;
    private EditText priceInput;
    private NumberPicker amount;
    private EditText unit;
    private String itemName;
    private String unitName;
    private double itemPrice;
    private double itemQty;
    private Uri imageUri, croppedImageUri;
    private String image;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_item, null);
        nameInput = view.findViewWithTag("editItemName");
        priceInput = view.findViewWithTag("editItemPrice");
        unit = view.findViewWithTag("editItemUnit");
        amount = view.findViewWithTag("editItemAmount");
        ImageView image = view.findViewWithTag("itemImageView");

        amount.setMinValue(1);
        amount.setMaxValue(50);
        amount.setValue(15);
        //amount.setTextSize(20);

        builder.setView(view)
                .setTitle("Add Item")
                // Add action buttons
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        itemName = nameInput.getText().toString();
                        itemPrice = Double.parseDouble(priceInput.getText().toString());
                        itemQty = amount.getValue();
                        //unhandled unit name
                        unitName = unit.getText().toString();
                        listener.sendItemName(itemName, itemPrice, (int) itemQty, croppedImageUri);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddItemDialog.ItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement ItemDialogListener");
        }
    }
    //interface for returning values to previous activity
    public interface ViewItemDialogListener{
        void sendItemName(String itemName);
    }
    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        // The result data contains a URI for the document or directory that
        // the user selected.
        imageUri = null;
        if (resultData != null) {
            imageUri = resultData.getData();
            image = getFileNameByUri(imageUri);
            //imageName.setText(image);
        }

        try
        {
            File temp_file = File.createTempFile("image", ".jpg");
            Log.e("Epp", temp_file.canRead() + " " + temp_file.canWrite() + " " + temp_file.canExecute());
            croppedImageUri = Uri.fromFile(temp_file);
        }
        catch(Exception e)
        {
            Log.d("whoops! ", "file exception");
        }
        Log.d("dj", imageUri.toString());
        UCrop.of(imageUri, croppedImageUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start(getActivity());


        if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(resultData);
            Log.d("Epp", resultUri.toString());

        }
    }
    public String getFileNameByUri(Uri uri)
    {
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().compareTo("content")==0)
        {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment();
            }
        }
        else if (uri.getScheme().compareTo("file")==0)
        {
            fileName = filePathUri.getLastPathSegment();
        }
        else
        {
            fileName = fileName+"_"+filePathUri.getLastPathSegment();
        }
        return fileName;
    }
}
