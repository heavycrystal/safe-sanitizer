package com.example.trial;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
//import android.support.v4.graphics.drawable.DrawableCompat#setTint;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private HashMap<String, ArrayList<String>> itemMap=null;
    private String[] mDataset=null;
    private RecyclerView recyclerView;
    private Activity ctx;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View ll;
        public Button item;
        public TextView itemPrice;
        public TextView itemQty;
        public MyViewHolder(View v) {
            super(v);
            ll = v;
            item = ll.findViewById(R.id.item);
            itemPrice = ll.findViewById(R.id.priceView);
            itemQty = ll.findViewById(R.id.qtyView);
        }
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] mDataset, HashMap<String, ArrayList<String>> itemMap) {
        this.mDataset = mDataset;
        this.itemMap = itemMap;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View ll = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        return new MyViewHolder(ll);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String item = mDataset[position];
        holder.item.setText(item);
        holder.itemPrice.setText("Price: "+itemMap.get(item).get(0));
        holder.itemQty.setText("Qty: "+itemMap.get(item).get(1));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItemDialog(view);
            }

        });

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
    public void viewItemDialog(View view){
        Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_and_edit_item);
        dialog.show();
    }

}