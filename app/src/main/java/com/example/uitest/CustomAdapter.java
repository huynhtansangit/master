package com.example.uitest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<ItemObj> voteItems;

    public CustomAdapter(Context context, int resource, List voteItems) {
        super(context, resource, voteItems);
        this.context = context;
        this.resource = resource;
        this.voteItems=voteItems;
   }

    public CustomAdapter(Context context, int resource, List objects, Context context1, int resource1, List<ItemObj> voteItems) {
        super(context, resource, objects);
        this.context = context1;
        this.resource = resource1;
        this.voteItems = voteItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(resource,null);//R.layout.vertical_itemview
        ImageView img = view.findViewById(R.id.Img);
        TextView title=view.findViewById(R.id.title);
        TextView detail=view.findViewById(R.id.detail);
        ItemObj vote= voteItems.get(position);
        Glide.with(view).load(vote.getUrl_image()).error(R.drawable.no_img).into(img);
        view.setTag(vote.getId());
        title.setText(vote.getTitle());
        detail.setText(vote.getCost()+" VNĐ per day");
        return view;
    }

}
