package com.example.uitest;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class support_fragment extends Fragment {
Button btnGuide_forHost,btnGuide_forClient,btnGuide_changeInfo;

    public support_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_support_fragment, container, false);
        btnGuide_changeInfo=view.findViewById(R.id.btn_wannaChangeInfo);
        btnGuide_forClient=view.findViewById(R.id.btn_wannaRent);
        btnGuide_forHost=view.findViewById(R.id.btn_wannaPost);
        btnGuide_forHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGuide ("Host");
            }
        });
        btnGuide_forClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGuide ("Client");
            }
        });
        btnGuide_changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGuide("Another");
            }
        });
        return view;
    }
    public void dialogGuide (String forWhat)
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.support_dialog);

        dialog.setCanceledOnTouchOutside(false);
        ImageView imageView =(ImageView) dialog.findViewById(R.id.iv_guide);
        Button btnGotit=(Button)dialog.findViewById(R.id.btn_gotit);
        if(forWhat =="Host")
        {
            dialog.setTitle("Owner");
            imageView.setImageResource(R.drawable.guideforhost);
        }
        else if (forWhat=="Client") {
            dialog.setTitle("Traveler");
            imageView.setImageResource(R.drawable.guideforclient);
        }
        else {
            dialog.setTitle("For editting");
            imageView.setImageResource(R.drawable.guideforchange);
        }
        btnGotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
