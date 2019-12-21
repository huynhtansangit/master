package com.example.uitest.ui.main;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uitest.R;
import com.example.uitest.tab1;
import com.example.uitest.tab2;
import com.example.uitest.tab3;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private String usr_session;
    public SectionsPagerAdapter(Context context, FragmentManager fm, String usr_ss) {
        super(fm);
        mContext = context;
        usr_session=usr_ss;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:{
                tab1 t1=new tab1();
                Bundle bundle=new Bundle();
                bundle.putString("usr_ss",usr_session);
                t1.setArguments(bundle);
                return t1;
            }
            case 1:{
                tab2 t1=new tab2();
                Bundle bundle=new Bundle();
                bundle.putString("usr_ss",usr_session);
                t1.setArguments(bundle);
                return t1;
            }
            case 2: {
                tab3 t1=new tab3();
                Bundle bundle=new Bundle();
                bundle.putString("usr_ss",usr_session);
                t1.setArguments(bundle);
                return t1;
            }
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}