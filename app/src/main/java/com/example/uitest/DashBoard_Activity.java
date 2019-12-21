package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.uitest.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class DashBoard_Activity extends Fragment {

    private String UserSession;
    TextInputLayout til_search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dash_board_);
        View view =inflater.inflate(R.layout.activity_dash_board_,container,false);
        setUserSession();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getChildFragmentManager(),getUserSession());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        til_search = view.findViewById(R.id.til_search);
        Button btn_Search=view.findViewById(R.id.btn_search);
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput())
                {
                    Intent intent=new Intent(getContext(),postslist.class);
                    //intent.putExtra("postURL",post_url);
                    intent.putExtra("sort_Type","s"+til_search.getEditText().getText().toString().trim());
                    getContext().startActivity(intent);
                }
                else {

                }
            }
        });
        return view;
    }
    public boolean validateInput ()
    {
        String input= til_search.getEditText().getText().toString();
        if(input.isEmpty())
        {
            til_search.setError("Must be filled");
            return false;
        }
        return true;
    }

    public void setUserSession(){
        UserSession="xxx";
    }
    public String getUserSession(){
        return UserSession;
    }
}
