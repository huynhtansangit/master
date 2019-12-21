package com.example.uitest;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class contact_fragment extends Fragment {

    LinearLayout linearLayout;
    public contact_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contact_fragment, container, false);
        linearLayout=v.findViewById(R.id.linearLayout_contact);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return v;
    }
    public void showDialog ()
    {
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.aboutus_layout);
        dialog.show();
    }
    public void dummyCLick(View view)
    {

    }
}
