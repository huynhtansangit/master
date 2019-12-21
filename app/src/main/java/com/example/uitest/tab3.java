package com.example.uitest;


import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tab3 extends Fragment {
    private String usr_session;
    private List<ItemObj> Items;
    ListView listView;
    Intent intent;
    UserInterface userInterface=ApiManager.getServiceUserInterface();;
    TokenUser tokenUser;
    CustomAdapter adapter;
    private PullRefreshLayout pullRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        usr_session=bundle.getString("usr_ss");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab3_frag, container, false);
        tokenUser=(TokenUser)getActivity().getApplicationContext();
        GetList();
        Button PostBtn=view.findViewById(R.id.newpost);
        pullRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread=new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            GetList();
                            pullRefreshLayout.setRefreshing(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        PostBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edittedProfile()){
                            GotoUpload();
                        }
                        else {
                            new ApiManager().showWarning(getContext(),"Fill out full infomations before posting");
                        }
                    }
                }
        );

        listView= view.findViewById(R.id.list_item);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // đi đến post
            }
        });
        return view;
    }
    boolean edittedProfile ()
    {
        String address = tokenUser.getUserOnline().getAddress();
        String phone = tokenUser.getUserOnline().getPhone();
        String birthday = tokenUser.getUserOnline().getBirth_day();
        String gender = tokenUser.getUserOnline().getGender();
        if (address==null|phone==null|birthday==null|gender==null)
        {
            return false;
        }
        else return true;
    }
    public void GotoUpload() {
        Intent intent = new Intent(getContext(),UploadPost.class);
        startActivity(intent);
    }

    public void GetList(){

        Call<ResponseBody> call=userInterface.getProductsByUser("Bearer" + " " + tokenUser.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    try {
                        ArrayList<ItemObj> itemObjs=new ArrayList<ItemObj>();
                        String responseData = response.body().string();
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONArray array=jsonObject.getJSONArray("post");
                        for(int i=0; i<array.length(); i++) {
                            JSONObject dataObj = (JSONObject) array.get(i);
                            itemObjs.add(new Gson().fromJson(dataObj.toString(), ItemObj.class));
                        }
                        // Sort by updated time;
                        Collections.sort(itemObjs,new Comparator<ItemObj>() {
                            DateFormat df=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                            @Override
                            public int compare(ItemObj u1, ItemObj u2) {
                                try {
                                    return df.parse(u2.getUpdated_at()).compareTo(df.parse(u1.getUpdated_at()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        // Sort done.
                        adapter =new CustomAdapter(getContext(),R.layout.post_item,itemObjs);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(getContext(),Post.class);
                                //intent.putExtra("postURL",post_url);
                                intent.putExtra("id",view.getTag().toString());
                                startActivity(intent);

                                // đi đến post
                            }
                        });
                        listView.setAdapter(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
