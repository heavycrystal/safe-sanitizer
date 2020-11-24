package com.example.reminder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyArrayAdapter extends ArrayAdapter {

    private final Context context;
    private final int layoutRes;
    private final ArrayList<ToDoPojo> arrayList;

    private final LayoutInflater inflater;

    public MyArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ToDoPojo> arrayList) {
        super(context, resource, arrayList);
        this.context=context;
        layoutRes=resource;
        this.arrayList=arrayList;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view=inflater.inflate(layoutRes,null);

        TextView date= view.findViewById(R.id.textViewDate);
        TextView time= view.findViewById(R.id.textViewTime);
        TextView name= view.findViewById(R.id.textViewName);
        TextView alpha= view.findViewById(R.id.textViewAlpha);
        CircleImageView imageView= view.findViewById(R.id.profile_image);

        ToDoPojo toDoPojo=arrayList.get(position);
        imageView.setImageResource(toDoPojo.getImageRes());
        name.setText(toDoPojo.getName());
        time.setText(toDoPojo.getTime());
        date.setText(toDoPojo.getDate());
        alpha.setText(toDoPojo.getAlpha());

        return view;
    }
}
