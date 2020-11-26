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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ViewItemDialog extends AppCompatDialogFragment {
    private static final int PICK_FILE = 14;
    private DBHelper db;
    private static String USERNAME, CATEGORY, ITEM;
    private ArrayList<String> itemData;

    private ViewItemDialogListener listener;
    private EditText nameInput;
    private EditText priceInput;
    private EditText unit;
    private Button uploadButton, upBtn, downBtn;
    private Button whatsApp, eMail;
    private TextView upNum, midNum, downNum;

    //initialize to values from database
    private String itemName;
    private int itemPrice;
    private int itemQty;
    private String itemUnit;
    private Uri imageUri, croppedImageUri;
    private String image, message;
    ImageView imageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_and_edit_item, null);
        db = new DBHelper(getContext());
        itemData = db.getItemData(USERNAME, CATEGORY, ITEM);

        //get existing data from database
        itemName=ITEM;
        itemPrice=Integer.parseInt(itemData.get(0));
        itemQty=Integer.parseInt(itemData.get(1));
        itemUnit = itemData.get(2);
        imageUri=Uri.parse(itemData.get(3));
        croppedImageUri=null;
        image=getFileNameByUri(imageUri);

        //enter data into dialog box
        nameInput = view.findViewById(R.id.editItemName);
        nameInput.setHint(itemName);
        priceInput = view.findViewById(R.id.priceView);
        priceInput.setHint(String.valueOf(itemPrice));

        upNum = view.findViewById(R.id.upNum);
        upNum.setText(String.valueOf(itemQty-1));
        midNum = view.findViewById(R.id.midNum);
        midNum.setText(String.valueOf(itemQty));
        downNum = view.findViewById(R.id.downNum);
        downNum.setText(String.valueOf(itemQty+1));

        unit = view.findViewById(R.id.editItemUnit);
        unit.setText(itemUnit);
        imageView = view.findViewById(R.id.imageView);
        Glide.with(getContext()).load(imageUri).centerCrop().into(imageView);
        imageView.setBackgroundResource(R.drawable.details_background);

        //making the CustomNumberPicker
        {
            upBtn = view.findViewById(R.id.upBtn);
            downBtn = view.findViewById(R.id.downBtn);

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
        uploadButton = view.findViewById(R.id.uploadItemImage);
        uploadButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        //onClickListener for sharing via Whatsapp
        whatsApp = view.findViewById(R.id.whatsApp);
        whatsApp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareViaWhatsapp();
            }
        });

        //onClickListener for sharing via e-mail
        eMail = view.findViewById(R.id.eMail);
        eMail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> emailBody = getEmailContent();
            }
        });

        builder.setView(view)
                .setTitle("Edit Item")
                // Add action buttons
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(!nameInput.getText().toString().equals(""))
                            itemName=nameInput.getText().toString();
                        if(!priceInput.getText().toString().equals(""))
                            itemPrice = Integer.parseInt(priceInput.getText().toString());
                        itemUnit = unit.getText().toString();
                        itemQty = Integer.parseInt(midNum.getText().toString());
                            Log.d("QTY", String.valueOf(itemQty));

                        if(!itemName.equals("") && itemPrice!=0 && itemQty!=0){
                            if(croppedImageUri!=null)
                                listener.sendEditedItemName(itemName, itemPrice, itemQty, itemUnit, croppedImageUri);
                            else
                                listener.sendEditedItemName(itemName, itemPrice, itemQty, itemUnit, imageUri);
                        }
                        else
                            Toast.makeText(getContext(), "Please enter all values", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.sendDeletedItemName(ITEM);
                }

        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ViewItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" " +
                    "must implement ItemDialogListener");
        }
    }
    public static void selectedItem(String username, String category, String item){
        USERNAME = username;
        CATEGORY = category;
        ITEM = item;
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
            Glide.with(getContext()).load(imageUri).centerCrop().into(imageView);
        }

        try
        {
            File temp_file = File.createTempFile("image", ".jpg");
            Log.e("Epp", temp_file.canRead() + " " + temp_file.canWrite() + " " + temp_file.canExecute());
            croppedImageUri = Uri.fromFile(temp_file);
        }
        catch(Exception e)
        {
            Log.d("whoops!", "file exception");
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
    //interface for returning values to previous activity
    public interface ViewItemDialogListener{
        void sendEditedItemName(String itemName, double itemPrice, int itemQty, String unitName, Uri imageUri);
        void sendDeletedItemName(String itemName);
    }
    private void shareViaWhatsapp(){
        message = itemName+"\n"
                +"Price: "+itemPrice+"\n"
                +"Quantity: "+itemQty+" "+itemUnit;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");

        File file = new File(imageUri.getPath());
        Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(Intent.EXTRA_STREAM,photoURI);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }
    private ArrayList<String> getEmailContent(){
        ArrayList<String> content = new ArrayList<>();
        String str;
        str="Item: "+itemName;
        content.add(str);
        str="Price: "+itemPrice;
        content.add(str);
        str="Quantity: "+itemQty+" "+itemUnit;
        content.add(str);
        return content;
    }
}
