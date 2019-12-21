package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

public class sorts_list extends AppCompatActivity {
    private String ussersSession;
    private String sortType="all";
    private  TextView txt;
    private LinearLayout linearLayout,linearLayouttoolbar;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorts_list);
        findViewById(R.id.ImgBtn_backArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        sharedPreferences=sorts_list.this.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        ussersSession = getIntent().getStringExtra("userSession");
        sortType = getIntent().getStringExtra("sort_Type");
        ListView listView=findViewById(R.id.sort_listview);
        linearLayout=findViewById(R.id.linearInSortList);
        linearLayouttoolbar=findViewById(R.id.ln_sortlisttoobar);
        SortItems adapter =new SortItems(this,R.layout.normalitemview,GetCategoryFromType(sortType));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      TextView tv= view.findViewById(R.id.normal_tv);
                      Intent intent = new Intent();
                      intent.setClass(sorts_list.this,postslist.class);
                      intent.putExtra("userSession",ussersSession);
                      intent.putExtra("sort_Type", tv.getTag().toString());
                      startActivity(intent);

               }
        });
        loadThemes (sorts_list.this);
    }

    public void loadThemes (Context context)
    {

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
        linearLayout.setBackgroundResource(R.drawable.summer_draw_backg);
        linearLayouttoolbar.setBackgroundResource(R.drawable.gradient_summer);
    }
    private void setBackgroundForNewYear ()
    {
        linearLayout.setBackgroundResource(R.drawable.newyear_background);
        linearLayouttoolbar.setBackgroundResource(R.drawable.gradient_newyear);
    }
    private void setBackgroundForClassic ()
    {
        linearLayout.setBackgroundResource(R.drawable.classical_background);
        linearLayouttoolbar.setBackgroundResource(R.drawable.gradient_vintage);
    }
    public void close(){
        this.finish();
    }
    public List<Item> GetCategoryFromType(String Tag){
        List<Item> list=new ArrayList<Item>();
        switch (Tag){
            case "price": {
                //list=new String[]{"0-500k","500k-1000k","1000k-1500k","1500k-2000k",
                //        "3000k-3500k","3500k-4000k","4000k-5000k","trên 5000k",};
                list.add(new Item("low cost","low"));
                list.add(new Item("medium cost","medium"));
                list.add(new Item("high cost","high"));
                list.add(new Item("for richkid","vip"));

            } break;
            case "no_seat": {
                list.add(new Item("4 seats","4"));
                list.add(new Item("7 seats","7"));
                list.add(new Item("16 seats","16"));
                list.add(new Item("29 seats","29"));
                list.add(new Item("45 seats","45"));
            } break;
            default: return null;
        }
        return list;
    }
    public class Item{
        private String name;
        private String tag;
        public Item(String _name, String _tag){
            this.name=_name;
            this.tag=_tag;
        }

        public String getName() {
            return name;
        }

        public String getTag() {
            return tag;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
    public class SortItems extends ArrayAdapter {
        Context context;
        List<Item> items;
        int resource;
        public SortItems(Context context, int resource, List<Item> items) {
            super(context, resource, items);
            this.context=context;
            this.items=items;
            this.resource=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view= inflater.inflate(R.layout.normalitemview,null);
           txt= view.findViewById(R.id.normal_tv);
            txt.setTag(items.get(position).getTag());
            txt.setText(items.get(position).getName());
            switch (sharedPreferences.getString("selection","null")) {
                case "summer":
                    txt.setTextColor(Color.parseColor("#ffddb0"));
                    break;
                    default:
                        break;
            }
            return view;
        }
    }
}
