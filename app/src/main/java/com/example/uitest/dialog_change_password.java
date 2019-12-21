package com.example.uitest;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dialog_change_password extends AppCompatDialogFragment {
    private TextInputLayout til_CurrentPass,til_NewPass,til_RetypePass;
    private Button btn_submit, btn_cancel;
    private ProgressBar progressBar;
    private TokenUser tokenUser;
    private String passwordtoCompare,currentPass,newPass,retypePass,token;
    private UserInterface userInterface=ApiManager.getServiceUserInterface();
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
           // "(?=.*[a-zA-Z])" +      //any letter
            //"(?=.*[!@#$%^&()+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 4 characters
            "$");
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.changepassword_dialog,null);
        builder.setView(view);
        findView(view);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmInput(view)){
                    progressBar.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.GONE);
                    currentPass=til_CurrentPass.getEditText().getText().toString().trim();
                    newPass=til_NewPass.getEditText().getText().toString().trim();
                    retypePass=til_RetypePass.getEditText().getText().toString().trim();
                    changePassword(currentPass,newPass,retypePass,token);
                }
                else {

                }
            }
        });
btn_cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        getDialog().dismiss();
    }
});
        return builder.create();
    }
    private void findView (View view)
    {
         tokenUser=(TokenUser)getActivity().getApplicationContext();
         passwordtoCompare=tokenUser.getPassword();
         token=tokenUser.getToken();
        til_CurrentPass=view.findViewById(R.id.til_currentpassword);
        til_NewPass=view.findViewById(R.id.til_newpassword);
        til_RetypePass=view.findViewById(R.id.til_retypenew);
        btn_cancel=view.findViewById(R.id.btn_Cancel);
        btn_submit=view.findViewById(R.id.btn_submit);
        progressBar=view.findViewById(R.id.spinkit_progress);
        ThreeBounce wave=new ThreeBounce();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.GONE);
    }
    private void changePassword (String current, final String newPass, String retype, String tokenAuth)
    {
        HashMap<String,Object> map=new HashMap<>();
        map.put("current_password",current);
        map.put("new_password",newPass);
        map.put("new_password_confirmation",retype);
        Call<ResponseBody> post=userInterface.changePassword("Bearer"+" "+tokenAuth,map);
        post.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                        TokenUser tokenUser =(TokenUser)getActivity().getApplicationContext();
                        tokenUser.setPassword(newPass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Thread thread =new Thread(){
                            @Override
                            public void run() {
                                try {
                                    sleep(2500);
                                    getDialog().dismiss();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };thread.start();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validatecurrentPass() {
        String currentPassInput = til_CurrentPass.getEditText().getText().toString().trim();
        if (currentPassInput.isEmpty()) {
            til_CurrentPass.setError("Field can't be empty");
            return false;
        } else if (!currentPassInput.equals(passwordtoCompare.trim())) {
            til_CurrentPass.setError("Current password's incorrect");
            return false;
        } else {
            til_CurrentPass.setError(null);
            return true;
        }
    }
    private boolean validateNewpassword() {
        String newpassInput = til_NewPass.getEditText().getText().toString().trim();
        if (newpassInput.isEmpty()) {
            til_NewPass.setError("Field can't be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(newpassInput).matches()) {
            til_NewPass.setError("Password too weak");
            Toast.makeText(getContext(), "Password has no space\nAt least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newpassInput.trim().equals(passwordtoCompare.trim()))
        {
            til_NewPass.setError("Must different from the old password");
            return  false;
        }
        else {
            til_NewPass.setError(null);
            return true;
        }
    }
    private boolean validateRetypePass() {
        String retypePassinput = til_RetypePass.getEditText().getText().toString().trim();
        if (retypePassinput.isEmpty()) {
            til_RetypePass.setError("Field can't be empty");
            return false;
        } else if (!retypePassinput.equals(til_NewPass.getEditText().getText().toString().trim())) {
            til_RetypePass.setError("Not match");
            return false;
        }
        else {
            til_RetypePass.setError(null);
            return true;
        }
    }
    private boolean confirmInput(View v) {
        if (!validatecurrentPass()|!validateNewpassword()|!validateRetypePass()) {
            return false;}
        else return true;
    }
}
