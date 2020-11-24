package gupta.p.todo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gupta.p.todo.Pojo.ToDoPojo;
import gupta.p.todo.R;


public class MyArrayAdapter extends ArrayAdapter {

    private final int layoutRes;
    private final ArrayList<ToDoPojo> arrayList;

    private final LayoutInflater inflater;

    public MyArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ToDoPojo> arrayList) {
        super(context, resource, arrayList);
        layoutRes=resource;
        this.arrayList=arrayList;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        @SuppressLint("ViewHolder") View view=inflater.inflate(layoutRes,null);

        TextView date= (TextView) view.findViewById(R.id.textViewDate);
        TextView time= (TextView) view.findViewById(R.id.textViewTime);
        TextView name= (TextView) view.findViewById(R.id.textViewName);
        TextView alpha= (TextView) view.findViewById(R.id.textViewAlpha);
        CircleImageView imageView= (CircleImageView) view.findViewById(R.id.profile_image);

        ToDoPojo toDoPojo=arrayList.get(position);
        imageView.setImageResource(toDoPojo.getImageRes());
        name.setText(toDoPojo.getName());
        time.setText(toDoPojo.getTime());
        date.setText(toDoPojo.getDate());
        alpha.setText(toDoPojo.getAlpha());

        return view;
    }
}
