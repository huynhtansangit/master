package com.example.uitest;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class postslist extends AppCompatActivity {
    private String tag;
    public List<ItemObj> Items;
    private UserInterface userInterface=ApiManager.getServiceUserInterface();
    private boolean isShow;
    public ListView listView;
    LinearLayout line,toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postslist);
        findViewById(R.id.ImgBtn_backArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyClick();
            }
        });
        line=findViewById(R.id.linearLayoutInPostlist);
        toolbar=findViewById(R.id.linearLayouttoolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getActionBar().setDisplayShowHomeEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayShowTitleEnabled(false);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color_toobar));
        tag= getIntent().getStringExtra("sort_Type");
        listView=findViewById(R.id.verticalList);
        getProducts (tag);
        loadThemes (postslist.this);
    }
    public void getProducts (final String tag)
    {
        final TokenUser tokenUser=(TokenUser)getApplicationContext();
        Call<List<ItemObj>> call= userInterface.getProductsByTag("Bearer" + " " + tokenUser.getToken(),tag);
        call.enqueue(new Callback<List<ItemObj>>() {
            @Override
            public void onResponse(Call<List<ItemObj>> call, Response<List<ItemObj>> response) {
                if(response.isSuccessful())
                {
                    ToastFollowTag(tag,postslist.this);
                    CustomAdapter adapter=new CustomAdapter(getApplicationContext(),R.layout.post_item,response.body());
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(getApplicationContext(),Post.class);
                            //intent.putExtra("postURL",post_url);
                            intent.putExtra("id",view.getTag().toString());
                            startActivity(intent);

                            // đi đến post
                        }
                    });
                }
                else {
                    try {
                        new ApiManager().showWarningClose(postslist.this,new JSONObject(response.errorBody().string()).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ItemObj>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error occured "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ToastFollowTag (String tag, Activity activity) {

        if ( tag.equals("low")){
            Toast.makeText(activity, "Cost between from 0 VNĐ to 500K VNĐ", Toast.LENGTH_SHORT).show();
    }
        else if ( tag.equals("medium")) {
            Toast.makeText(activity, "Cost between from 500K VNĐ to 1500K VNĐ", Toast.LENGTH_SHORT).show();
        }
        else if ( tag.equals("high")) {
            Toast.makeText(activity, "Cost between from 1500K VNĐ to 3000K VNĐ", Toast.LENGTH_SHORT).show();
        }
        else if ( tag.equals("vip")) {
            Toast.makeText(activity, "Cost between from 3000K VNĐ to 5000K VNĐ", Toast.LENGTH_SHORT).show();
        }
        else if (tag.contains("s")) {
            Toast.makeText(activity, "All items has '"+tag.substring(1)+"' in Title field", Toast.LENGTH_SHORT).show();
        }
        else
        {

        }
    }

    public void loadThemes (Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        switch (sharedPreferences.getString("selection","null"))
        {
            case "summer":
                setBackgroundForSummer();
                break;
            case "classic":
                setBackgroundForClassic ();
                break;
            case "newyear":
                setBackgroundForNewYear ();
                break;
            default:
                break;
        }
    }

    private void setBackgroundForSummer ()
    {
        line.setBackgroundResource(R.drawable.summer_navi);
        toolbar.setBackgroundResource(R.drawable.gradient_summer);
    }
    private void setBackgroundForNewYear ()
    {
        line.setBackgroundResource(R.drawable.navi_newyear);
        toolbar.setBackgroundResource(R.drawable.gradient_newyear);
    }
    private void setBackgroundForClassic ()
    {
        line.setBackgroundResource(R.drawable.navi_classic);
        toolbar.setBackgroundResource(R.drawable.gradient_vintage);
    }

    public void dummyClick ()
    {
        this.finish();
    }
}
