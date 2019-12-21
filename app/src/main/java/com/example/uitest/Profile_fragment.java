package com.example.uitest;


import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_fragment extends Fragment implements  EditDialogListener {
    private TextView tv_address,tv_phone,tv_gender,tv_birthday,tv_nameinProfileFrag,tv_CreatedDateInProfileFrag;
    private ImageView iv_avt_pro_frag;
    private Button btnEdit,btnChangepass;
    private Current_user_cache User_cache;
    private String token;
    private LinearLayout layout;
    UserInterface userInterface=ApiManager.getServiceUserInterface();
    @Override
    public void sendStringInput(String key, String message) {
        if(key == "address")
        {
            tv_address.setText(message);
        }
        if(key == "username")
        {
            tv_nameinProfileFrag.setText(message);
        }
        if(key == "phone")
        {
            tv_phone.setText(message);
        }
        if(key == "gender")
        {
            tv_gender.setText(message);
        }
        if(key == "birthday")
        {
            tv_birthday.setText(message);
        }
        if (key=="change")
        {
            tv_gender.setText(message);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view= inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        findView(view);
        getInfoUser();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditDialog();
            }
        });
        btnChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangePassDialog();
            }
        });

        return view;
    }
    public void openEditDialog()
    {
        dialog_edit_info dialogEditInfo=new dialog_edit_info();
        dialogEditInfo.setTargetFragment(this,1);
        dialogEditInfo.show(getFragmentManager(),"Edit_dialog");
    }
    public void openChangePassDialog()
    {
        dialog_change_password dialogChangePassword=new dialog_change_password();
        dialogChangePassword.setTargetFragment(this,1);
        dialogChangePassword.show(getFragmentManager(),"ChangePassword_dialog");
    }
    private void findView (View view)
    {
        TokenUser currentToken= (TokenUser)getActivity().getApplicationContext();
        token=currentToken.getToken();
        tv_address= view.findViewById(R.id.tv_youraddress);
        tv_birthday= view.findViewById(R.id.tv_birthday);
        tv_gender= view.findViewById(R.id.tv_gender);
        tv_phone= view.findViewById(R.id.tv_yourphone);
        iv_avt_pro_frag=view.findViewById(R.id.im_Avt_profile);
        btnEdit=view.findViewById(R.id.btn_edit_profile);
        tv_nameinProfileFrag=view.findViewById(R.id.tv_fragYourname);
        tv_CreatedDateInProfileFrag=view.findViewById(R.id.tv_FragdatebeginUsing);
        btnChangepass=view.findViewById(R.id.btn_changePassword);
        layout=view.findViewById(R.id.linearInProfile);
    }
    private void loadImageintoAvatar (String link,final ImageView imageView )
    {
        Glide.with(this).load(link).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    private void initProfile () throws ParseException {
        if(User_cache.getAddress()==null)
        {
            tv_address.setText("Not filled yet");
        }
        else tv_address.setText(User_cache.getAddress().trim());

        if(User_cache.getBirth_day()==null)
        {
            tv_birthday.setText("Not filled yet");
        }
        else {
            String dateInString =User_cache.getBirth_day().trim();
            SimpleDateFormat formatter1 =new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat formatter2 =new SimpleDateFormat("dd/mm/yyyy");
            Date date=formatter1.parse(dateInString);

            tv_birthday.setText(formatter2.format(date));
        }
        if(User_cache.getGender()==null)
        {
            tv_gender.setText("Not filled yet");
        }
        else tv_gender.setText(User_cache.getGender().trim());
        if(User_cache.getPhone()==null)
        {
            tv_phone.setText("Not filled yet");
        }
        else tv_phone.setText(User_cache.getPhone().trim());
        loadImageintoAvatar(User_cache.getAvatar(),iv_avt_pro_frag);
        tv_nameinProfileFrag.setText(User_cache.getName());
        tv_CreatedDateInProfileFrag.setText(User_cache.getCreated_at());
    }


    private void getInfoUser ()
    {
        Call<ResponseBody> call =userInterface.getInfoUser("Bearer" + " " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {

                        JsonParser parser = new JsonParser();
                        JsonElement mJson =  parser.parse(response.body().string());
                        Gson gson = new Gson();
                        // convert json obj into class and send it
                        User_cache = gson.fromJson(mJson, Current_user_cache.class);
                        initProfile();
                        //Toast.makeText(getContext(), "  "+token, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else Toast.makeText(getContext(), "Token bad "+token, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Failure !", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
