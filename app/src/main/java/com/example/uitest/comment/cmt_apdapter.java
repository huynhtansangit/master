package com.example.uitest.comment;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.uitest.ApiManager;
import com.example.uitest.Current_user_cache;
import com.example.uitest.R;
import com.example.uitest.TokenUser;
import com.example.uitest.UserInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cmt_apdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<commentObj> cmtObj;
    UserInterface userInterface = ApiManager.getServiceUserInterface();
    TokenUser tokenUser;
    private void getUserOfComment (final ImageView avt, final TextView name, String user_id)
    {
        Call<Current_user_cache> call= userInterface.getUserbyId("Bearer" + " " + tokenUser.getToken(),user_id);
        call.enqueue(new Callback<Current_user_cache>() {
            @Override
            public void onResponse(Call<Current_user_cache> call, Response<Current_user_cache> response) {
                if (response.isSuccessful())
                {
                    name.setText(response.body().getName());
                    Glide.with(context).load(response.body().getAvatar()).apply(RequestOptions.circleCropTransform()).into(avt);
                }
            }

            @Override
            public void onFailure(Call<Current_user_cache> call, Throwable t) {
            }
        });
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(resource,null);
        tokenUser=(TokenUser)context.getApplicationContext();
        TextView name=view.findViewById(R.id.tv_namecmt),content=view.findViewById(R.id.tv_content);
        ImageView avtcmt=view.findViewById(R.id.avt_cmt);
        TextView datetime=view.findViewById(R.id.tv_time);

        commentObj commentObj= cmtObj.get(position);
        getUserOfComment(avtcmt,name,commentObj.getUser_id());
        datetime.setText(commentObj.getUpdated_at());
        content.setText(commentObj.getBody());

        return view;
    }

    public cmt_apdapter(@NonNull Context context, int resource, @NonNull List<commentObj> cmtObj) {
        super(context, resource, cmtObj);
        this.context=context;
        this.resource=resource;
        this.cmtObj=cmtObj;
    }

}
