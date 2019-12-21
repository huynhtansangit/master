package com.example.uitest;



import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
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

public class tab1 extends Fragment {
    private String usr_session;
    private UserInterface userInterface=ApiManager.getServiceUserInterface();
    private RecyclerView recyclerView1,recyclerView2,recyclerView3;
    private List<ItemObj> ItemArr;
    private PullRefreshLayout pullRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        usr_session=bundle.getString("usr_ss");
        //createRecycleView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1_frag, container, false);
        TokenUser tokenUser=(TokenUser)getActivity().getApplicationContext();
        //createRecycleView(view,R.id.rcleView1);
        //createRecycleView(view,R.id.rcleView2);
        //createRecycleView(view,R.id.rcleView3);
        //Context context=this.getContext();
        pullRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        recyclerView1= view.findViewById(R.id.rcleView1);
        recyclerView2= view.findViewById(R.id.rcleView2);
        recyclerView3= view.findViewById(R.id.rcleView3);
        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView3.setHasFixedSize(true);
        LinearLayoutManager manager1=new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager manager2=new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager manager3=new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        loadingProducts ();
        recyclerView1.setLayoutManager(manager1);
        recyclerView2.setLayoutManager(manager2);
        recyclerView3.setLayoutManager(manager3);
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView3.setNestedScrollingEnabled(false);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread=new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            loadingProducts ();
                            pullRefreshLayout.setRefreshing(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        return view;
    }

    /*public void createRecycleView(View view, int resource ){
        RecyclerView recyclerView= view.findViewById(resource);//R.id.rcleView
        ArrayList<ItemObj> list =new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        getProduct(list);
        cusAdapter4rv adapter4rv=new cusAdapter4rv(list,this.getContext(),usr_session);
        recyclerView.setAdapter(adapter4rv);
        LinearLayoutManager manager=new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
    }*/
    private static List<ItemObj> filterListByTimeUpdate(List<ItemObj> list)
    {
        return list;
    }
    private List<ItemObj> filterListByLowPrice(List<ItemObj> list)
    {
        List<ItemObj> res=list;
        for (int i=0;i<list.size();i++)
        {
            if (new ApiManager().getFloatFromPriceString(list.get(i).getCost())<500)
            {
                res.remove(i);
            }
        }
        /*Collections.sort(list,new Comparator<ItemObj>() {
            @Override
            public int compare(ItemObj u1, ItemObj u2) {
                return Float.compare(new ApiManager().getFloatFromPriceString(u1.getCost()),new ApiManager().getFloatFromPriceString(u2.getCost()));
            }
        });*/
        return res;
    }

    public void getProductsOrderByUpdateTime () {
        final TokenUser tokenUser = (TokenUser) getActivity().getApplicationContext();
        Call<List<ItemObj>> call = userInterface.getProductsByTag("Bearer" + " " + tokenUser.getToken(), "updatetime");
        call.enqueue(new Callback<List<ItemObj>>() {
            @Override
            public void onResponse(Call<List<ItemObj>> call, Response<List<ItemObj>> response) {
                if (response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        recyclerView2.setAdapter(new cusAdapter4rv(response.body(),getContext(),15));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ItemObj>> call, Throwable t) {
            }
        });
    }
    public void getProductsOrderByPrice () {
        final TokenUser tokenUser = (TokenUser) getActivity().getApplicationContext();
        Call<List<ItemObj>> call = userInterface.getProductsByTag("Bearer" + " " + tokenUser.getToken(), "low");
        call.enqueue(new Callback<List<ItemObj>>() {
            @Override
            public void onResponse(Call<List<ItemObj>> call, Response<List<ItemObj>> response) {
                if (response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        recyclerView3.setAdapter(new cusAdapter4rv(response.body(),getContext(),15));
                    }
                }
                else {
                    try {
                        new ApiManager().showError(getContext(),response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ItemObj>> call, Throwable t) {
                new ApiManager().showWarning(getContext(),t.getMessage());
            }
        });
    }
    public void getAllProducts () {
        final TokenUser tokenUser = (TokenUser) getActivity().getApplicationContext();
        Call<List<ItemObj>> call = userInterface.getProducts("Bearer" + " " + tokenUser.getToken());
        call.enqueue(new Callback<List<ItemObj>>() {
            @Override
            public void onResponse(Call<List<ItemObj>> call, Response<List<ItemObj>> response) {
                if (response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        recyclerView1.setAdapter(new cusAdapter4rv(response.body(),getContext(),15));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ItemObj>> call, Throwable t) {
            }
        });
    }

    private void loadingProducts ()
    {
        getAllProducts();
        getProductsOrderByPrice();
        getProductsOrderByUpdateTime();
    }
}
