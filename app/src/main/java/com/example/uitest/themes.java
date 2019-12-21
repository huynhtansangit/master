package com.example.uitest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.uitest.MainActivity.SHARE_PREFS;


/**
 * A simple {@link Fragment} subclass.
 */
public class themes extends Fragment {
    private Button summer,newyear,classic,btndefault;
    public themes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_themes, container, false);
        findView (view);

        SharedPreferences sharedPreferences=view.getContext().getSharedPreferences(SHARE_PREFS,Context.MODE_PRIVATE);
        switch (sharedPreferences.getString("selection","null")) {
            case "summer":
                summer.setTextColor(Color.parseColor("#e54304"));
                summer.setTextSize(24f);
                break;
            case "classic":
                classic.setTextColor(Color.parseColor("#e54304"));
                classic.setTextSize(24f);
                break;
            case "newyear":
                newyear.setTextColor(Color.parseColor("#e54304"));
                newyear.setTextSize(24f);
                break;
            default:
                btndefault.setTextColor(Color.parseColor("#e54304"));
                btndefault.setTextSize(24f);
                break;
        }
        summer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("You need re-login to apply change")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                logout1();
                                saveSelection("summer");
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        newyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("You need re-login to apply change")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                logout1();
                                saveSelection("newyear");
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("You need re-login to apply change")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                logout1();
                                saveSelection("classic");
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    btndefault.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("You need re-login to apply change")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            logout1();
                            saveSelection("null");
                        }
                    })
                    .setCancelText("No")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    });
        return view;
    }
    TokenUser tokenUser;
    UserInterface userInterface=ApiManager.getServiceUserInterface();
    public void logout1 ()
    {
        tokenUser=(TokenUser)getActivity().getApplicationContext();
        Call<ResponseBody> call= userInterface.Logout("Bearer" + " " + tokenUser.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() !=null)
                {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Sorry! "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changeBackground_Summer (Context context)
    {
        Activity activity=(Activity)context;
        DrawerLayout drawerLayout=activity.findViewById(R.id.Drawer_layout);
        drawerLayout.setBackgroundResource(R.drawable.summer_draw_backg);

        Toolbar toolbar=activity.findViewById(R.id.action_toolbar);
        toolbar.setBackgroundResource(R.drawable.gradient_summer);

        LinearLayout linearLayout =activity.findViewById(R.id.above_status_bar);
        linearLayout.setBackgroundResource(R.drawable.gradient_summer);

        NavigationView navigationView=activity.findViewById(R.id.navigattionview);
        navigationView.setItemBackgroundResource(R.drawable.gradient_summer);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#212121")));
        navigationView.setBackgroundResource(R.drawable.summer_navi);

    }
    public void changeBackground_NewYear (Context context)
    {
        Activity activity=(Activity)context;
        DrawerLayout drawerLayout=activity.findViewById(R.id.Drawer_layout);
        drawerLayout.setBackgroundResource(R.drawable.newyear_background);

        Toolbar toolbar=activity.findViewById(R.id.action_toolbar);
        toolbar.setBackgroundResource(R.drawable.gradient_newyear);

        LinearLayout linearLayout =activity.findViewById(R.id.above_status_bar);
        linearLayout.setBackgroundResource(R.drawable.gradient_newyear);

        NavigationView navigationView=activity.findViewById(R.id.navigattionview);
        navigationView.setItemBackgroundResource(R.drawable.gradient_newyear);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FFE0B2")));
        navigationView.setBackgroundResource(R.drawable.navi_newyear);

    }

    public void changeBackground_Classical (Context context)
    {
        Activity activity=(Activity)context;
        DrawerLayout drawerLayout=activity.findViewById(R.id.Drawer_layout);
        drawerLayout.setBackgroundResource(R.drawable.classical_background);

        Toolbar toolbar=activity.findViewById(R.id.action_toolbar);
        toolbar.setBackgroundResource(R.drawable.gradient_vintage);

        LinearLayout linearLayout =activity.findViewById(R.id.above_status_bar);
        linearLayout.setBackgroundResource(R.drawable.gradient_vintage);

        NavigationView navigationView=activity.findViewById(R.id.navigattionview);
        navigationView.setItemBackgroundResource(R.drawable.gradient_vintage);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FFE0B2")));
        navigationView.setBackgroundResource(R.drawable.navi_classic);

    }
    private void findView(View view)
    {
        summer=view.findViewById(R.id.summer);
        newyear=view.findViewById(R.id.newyear);
        classic=view.findViewById(R.id.classic);
        btndefault=view.findViewById(R.id.btn_default);
    }
    public static final String SHARE_PREFS="sharedPrefs";
    public void saveSelection (String selection)
    {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(SHARE_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("selection",selection);
        editor.apply();
    }
    public void loadThemes (Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARE_PREFS,Context.MODE_PRIVATE);
        switch (sharedPreferences.getString("selection","null"))
        {
            case "summer":
                changeBackground_Summer(context);
                break;
            case "classic":
                changeBackground_Classical(context);
                break;
            case "newyear":
                changeBackground_NewYear(context);
                break;
                default:
                    break;
        }
    }
}
