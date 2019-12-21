package com.example.uitest;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class cusAdapter4rv extends RecyclerView.Adapter<cusAdapter4rv.ViewHolder> {
    List<ItemObj> Items;
    private Context context;
    private TokenUser tokenUser;
    private String id_item;
    private UserInterface userInterface;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public cusAdapter4rv(List<ItemObj> items, Context context, int count) {
        Items = items;
        this.context = context;
        this.count=count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View item= inflater.inflate(R.layout.h_recycleview_item,viewGroup,false);
        return new ViewHolder(item);
    }

    private void getUserbyId (String id_user, final TextView textView)
    {
        userInterface=ApiManager.getServiceUserInterface();
        Call<Current_user_cache> call= userInterface.getUserbyId("Bearer" + " " + tokenUser.getToken(),id_user);
        call.enqueue(new Callback<Current_user_cache>() {
            @Override
            public void onResponse(Call<Current_user_cache> call, Response<Current_user_cache> response) {
                if(response.isSuccessful())
                {
                    textView.setText(response.body().getAddress());
                }
                else Toast.makeText(context, "response is fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Current_user_cache> call, Throwable t) {
                Toast.makeText(context, "something wrong "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d("onBindViewHolder","test Glide");
        viewHolder.textTitle.setText(Items.get(i).getTitle());
        float cost=new ApiManager().getFloatFromPriceString(Items.get(i).getCost());
        viewHolder.textCost.setText(cost+" K");
        viewHolder.textCost.bringToFront();
        getUserbyId(Items.get(i).getUser_id(),viewHolder.textAddr);
        viewHolder.textTitle.setTag(Items.get(i).getId());
        viewHolder.textUpdateTime.append(Items.get(i).getUpdated_at());
        Glide.with(context).load(Items.get(i).getUrl_image()).centerCrop().placeholder(R.drawable.no_img).into(viewHolder.img);
        /*viewHolder.img.setImageResource(R.drawable.no_img);*/
    }

    @Override
    public int getItemCount() {
        if(Items.size() > count){
            return count;
        }
        else
        {
            return Items.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textTitle,textCost ,textAddr,textUpdateTime;
        ImageView img;


        public ViewHolder(View view){
            super(view);
            tokenUser=(TokenUser)context.getApplicationContext();
            textTitle=view.findViewById(R.id.itemTitle);
            textCost=view.findViewById(R.id.itemPrice);
            textAddr=view.findViewById(R.id.item_address);
            textUpdateTime=view.findViewById(R.id.item_updatetime);
            img=view.findViewById(R.id.itemImg);
            //context = itemView.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            String post_url = textTitle.getTag().toString();
            Intent intent=new Intent(context,Post.class);
            //intent.putExtra("postURL",post_url);
            intent.putExtra("id",textTitle.getTag().toString());
            context.startActivity(intent);
        }
    }
}
