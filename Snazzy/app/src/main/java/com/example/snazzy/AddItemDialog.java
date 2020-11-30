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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AddItemDialog extends AppCompatDialogFragment {
    private static final int PICK_FILE = 14;
    private ItemDialogListener listener;
    private EditText nameInput;
    private EditText priceInput;
    private EditText unit;
    private NumberPicker amount;
    private Button uploadButton, upBtn, downBtn;
    private String itemName=null;
    private TextView imageName, upNum, midNum, downNum;
    private String unitName=null;
    private double itemPrice=0;
    private double itemQty=0;
    private Uri imageUri=null, croppedImageUri=null;
    private String image=null;
    private String defaultImage = ViewCategory.DEF_IMAGE;
    String suggestion;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item, null);

        nameInput = view.findViewById(R.id.editItemName);
        Log.d("nameInput", String.valueOf(nameInput==null));
        priceInput = view.findViewById(R.id.priceView);
        unit = view.findViewById(R.id.editItemUnit);
        imageName = view.findViewById(R.id.imageName);

        //making the CustomNumberClicker
        {

            upNum = view.findViewById(R.id.upNum);
            upNum.setText("14");
            midNum = view.findViewById(R.id.midNum);
            midNum.setText("15");
            downNum = view.findViewById(R.id.downNum);
            downNum.setText("16");
            upBtn = view.findViewById(R.id.upBtn);
            downBtn = view.findViewById(R.id.downBtn);

            Log.d("Done", "amount done");
            uploadButton = (Button) view.findViewById(R.id.uploadItemImage);
            Log.d("UploadButton", String.valueOf(uploadButton == null));

            upBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(downNum.getText().toString().equals(" ")){}
                    else if(Integer.parseInt(midNum.getText().toString())<49) {
                        String midNumber = midNum.getText().toString();
                        upNum.setText(midNumber);
                        midNum.setText(String.valueOf(Integer.parseInt(midNumber) + 1));
                        downNum.setText(String.valueOf(Integer.parseInt(midNumber) + 2));
                    }
                    else{
                        upNum.setText(String.valueOf(49));
                        midNum.setText(String.valueOf(50));
                        downNum.setText(" ");
                    }
                }
            });
            downBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(upNum.getText().toString().equals(" ")){}
                    else if(Integer.parseInt(midNum.getText().toString())>2) {
                        String midNumber = midNum.getText().toString();
                        upNum.setText(String.valueOf(Integer.parseInt(midNumber) - 2));
                        midNum.setText(String.valueOf(Integer.parseInt(midNumber) - 1));
                        downNum.setText(midNumber);
                    }
                    else{
                        upNum.setText(" ");
                        midNum.setText(String.valueOf(1));
                        downNum.setText(String.valueOf(2));
                    }
                }
            });
        }
        //setting onClickListener for uploadButton
        uploadButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        builder.setView(view)
                .setTitle("Add Item")
                // Add action buttons
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            itemName = nameInput.getText().toString();
                            itemPrice = Double.parseDouble(priceInput.getText().toString());
                            itemQty = Integer.parseInt(midNum.getText().toString());
                        }
                        catch(Exception e){
                            Toast.makeText(getContext(), "Please enter all values", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            unitName = unit.getText().toString();
                        }
                        catch(Exception e){
                            unitName = "";
                        }
                        if(itemName!=null && itemPrice!=0 && itemQty!=0){
                            if(croppedImageUri!=null)
                                listener.sendItemName(itemName, itemPrice, (int) itemQty, unitName, croppedImageUri);
                            else{
                                listener.sendItemName(itemName, itemPrice, (int) itemQty, unitName, Uri.parse(defaultImage));
                                Log.d("Default Image", defaultImage);
                            }
                        }
                        else
                            Toast.makeText(getContext(), "Please enter all values", Toast.LENGTH_SHORT).show();

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
            listener = (ItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement ItemDialogListener");
        }
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
            imageName.setText(image);


            try {
                File temp_file = File.createTempFile("default_item_image", ".jpg");
                croppedImageUri = Uri.fromFile(temp_file);
                Log.d("URI", croppedImageUri.toString());
            } catch (Exception e) {
                Log.d("whoops! ", "file exception");
            }
            Log.d("dj", imageUri.toString());
            UCrop.of(imageUri, croppedImageUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(500, 500)
                    .start(getActivity());
            suggestion = TensorflowTest.infer_image(imageUri, getContext());
            nameInput.setHint(suggestion);
            nameInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nameInput.getText() == null)
                        nameInput.setText(suggestion);
                }
            });
            Log.e("Tag", suggestion);

            if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                Toast.makeText(getContext(), "Toast works", Toast.LENGTH_SHORT).show();
                Uri resultUri = UCrop.getOutput(resultData);
                Log.d("Epp", resultUri.toString());
            }
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
    //interface for returning values to previous activity
    public interface ItemDialogListener{
        void sendItemName(String itemName, double itemPrice, int itemQty, String unitname, Uri imageUri);
    }
}
