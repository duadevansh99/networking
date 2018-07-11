package com.example.de.nerworking1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class postsAdapter extends ArrayAdapter {

    ArrayList<Post> objects;
    LayoutInflater inflater;

    public postsAdapter(@NonNull Context context, ArrayList<Post> objects) {
        super(context, 0, objects);
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects=objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output= convertView;
        if(output==null){
            output=inflater.inflate(R.layout.post_row_layout,parent,false);
            TextView titleTextView=output.findViewById(R.id.posttitles);
            postsViewHolder viewHolder=new postsViewHolder();
            viewHolder.title=titleTextView;
            output.setTag(viewHolder);
        }
        postsViewHolder viewHolder=(postsViewHolder) output.getTag();
        Post post=objects.get(position);
        viewHolder.title.setText(post.title);
        return output;
    }
}
