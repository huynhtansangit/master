package com.example.uitest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
private Button btn_login;
    private TextView tv_forgotpass,tv_register;
    private ImageView logo_inLogin;
    private EditText et_emailname,et_pass;
    private String token, email_sending,s_password;
    private CheckBox cb_showpass,remember_me;
    private ProgressBar prog_login;
    private UserInterface userInterface=ApiManager.getServiceUserInterface();
    private static String apiLogin ="api/login";
    private static String URL_LOGIN=ApiManager.getBaseURL()+apiLogin;
    private Current_user_cache User_cache;
    public TokenUser tokenUser;
    InputMethodManager inputManager ;
    public static final String SHARE_PREFS="sharedPrefs";
    private boolean stickCheckboxOnOff;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[!@#$%^&()+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        final SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        inputManager = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        tokenUser=((TokenUser)getApplicationContext());
        // remove status
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        cb_showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else et_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_intent=new Intent(getApplicationContext(),register.class);
                Pair [] pairs =new Pair[2];
                pairs[0]=new Pair<View,String>(logo_inLogin,"logoImage");
                pairs[1]=new Pair<View,String>(btn_login,"btnTrans");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(register_intent,activityOptions.toBundle());
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmInput(view))
                {
/*                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);*/
                    if (stickCheckboxOnOff)
                    {
                        saveAccount(et_emailname.getText().toString().trim()
                                ,et_pass.getText().toString().trim());
                    }
                        Login();
                        btn_login.setVisibility(View.GONE);
                        prog_login.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "invalidated input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // greeting for fun
        final Random r=new Random();
        logo_inLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=r.nextInt((4-1)+1)+1;
                switch (i)
                {
                    case 1:
                        Toast.makeText(MainActivity.this, "Nice to see you agian", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Bonne journée", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "È il nostro grande onore darvi il benvenuto", Toast.LENGTH_SHORT).show();
                        break;
                        default:
                            Toast.makeText(MainActivity.this, "Be our guest", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetDialog(MainActivity.this,et_emailname,et_pass);
            }
        });
        remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.NORMAL_TYPE)
                            .setContentText("Sure about saving your account ?")
                            .setConfirmText("Sure")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    stickCheckboxOnOff=true;
                                    if(validateUserEmail()&&validatePass()) {
                                        compoundButton.setChecked(true);
                                    }
                                    else compoundButton.setChecked(false);
                                    saveAccount(et_emailname.getText().toString().trim()
                                            ,et_pass.getText().toString().trim()); // save checkbox
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelText("No")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    stickCheckboxOnOff=false;
                                    compoundButton.setChecked(false);
                                    saveAccount(null,null);
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.show();
                }
                else {
                    stickCheckboxOnOff=false;
                    compoundButton.setChecked(false);
                    saveAccount(null,null);
                }
            }
        });
        if(sharedPreferences.getBoolean("checkbox",false))
        {
            loadAccount();
        }
    }
    // REad Json
    private void findView ()
    {
        logo_inLogin=(ImageView)findViewById(R.id.im_rentaline);
        btn_login=(Button)findViewById(R.id.btn_login);
        tv_forgotpass=(TextView)findViewById(R.id.tv_forgotpass);
        tv_register=(TextView)findViewById(R.id.tx_register);
        et_emailname=findViewById(R.id.et_username);
        et_pass=findViewById(R.id.et_password);
        cb_showpass=findViewById(R.id.cb_showpass);
        prog_login=findViewById(R.id.spinkitview_login);
        remember_me=findViewById(R.id.remember);
        CubeGrid cubeGrid=new CubeGrid();
        prog_login.setIndeterminateDrawable(cubeGrid);
        prog_login.setVisibility(View.GONE);

    }
    void saveAccount (String email, String password)
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putBoolean("checkbox",remember_me.isChecked());
        editor.apply();
    }
    void loadAccount ()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        et_emailname.setText(sharedPreferences.getString("email",""));
        et_pass.setText(sharedPreferences.getString("password",""));
        remember_me.setChecked(sharedPreferences.getBoolean("checkbox",false));
        stickCheckboxOnOff=sharedPreferences.getBoolean("checkbox",false);
    }

    private boolean validateUserEmail() {
        String usernameInput = et_emailname.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            et_emailname.setError("Field can't be empty");
            return false;
        }
        else {
            et_emailname.setError(null);
            return true;
        }
    }
    private boolean validatePass() {
        String passInput = et_pass.getText().toString().trim();

        if (passInput.isEmpty()) {
            et_pass.setError("Field can't be empty");
            return false;
        }
        else {
            et_pass.setError(null);
            return true;
        }
    }
    public boolean confirmInput(View v) {
        if (!validateUserEmail()||!validatePass()) {
            return false;}
        else return true;
    }
    private int GoneAll (TextInputLayout tiltoken,TextInputLayout tilpass,TextInputLayout tilcofm,LinearLayout linearLayout)
    {
        tilcofm.setVisibility(View.GONE);
        tilpass.setVisibility(View.GONE);
        tiltoken.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        return 0;
    }
    private int GoneAppearence (TextInputLayout tiltoken, TextInputLayout tilpass, TextInputLayout tilcofm, LinearLayout linearLayout)
    {
        tilcofm.setVisibility(View.VISIBLE);
        tilpass.setVisibility(View.VISIBLE);
        tiltoken.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        return 1;
    }
    private void showResetDialog (Activity activity, final EditText m_login, final EditText p_login)
    {
        View view =getLayoutInflater().inflate(R.layout.reset_dialog,null);
        final LinearLayout linearLayout1=view.findViewById(R.id.linearLayout1);
        final LinearLayout linearLayout2=view.findViewById(R.id.linearLayout2);
        Button btn_sendmail =view.findViewById(R.id.btn_sendMail);
        Button btn_submit =view.findViewById(R.id.btn_submit);
        Button btn_cancel =view.findViewById(R.id.btn_Cancel);
        Button btn_back =view.findViewById(R.id.btn_back);
        final TextInputLayout email=view.findViewById(R.id.til_email);
        final TextInputLayout otp=view.findViewById(R.id.til_token);
        final TextInputLayout password=view.findViewById(R.id.til_pass);
        final TextInputLayout confirm=view.findViewById(R.id.til_confirmpass);
        // --dialog reset password
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Reset password")
           .setView(view).setMessage("Please enter your email to request a password reset!");
        final AlertDialog dialog=builder.create();
        dialog.show();
        GoneAll(otp,password,confirm,linearLayout2);

        btn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(validEmail(email))
                    {
                        sendMailToReset(email.getEditText().getText().toString()
                                ,otp
                                ,password
                                ,confirm
                                ,linearLayout1
                                ,linearLayout2);
                    }
                    else {}
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatorline1back(linearLayout1,1000);
                GoneAll(otp,password,confirm,linearLayout2);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validEmail(email)| !validOTP(otp)|!validNewPass(password)|!validRetype(confirm,password.getEditText().getText().toString().trim()))
                {}
                else {
                    String mail=email.getEditText().getText().toString().trim();
                    String token=otp.getEditText().getText().toString().trim();
                    String newpass=password.getEditText().getText().toString().trim();
                    String confirmation=confirm.getEditText().getText().toString().trim();
                    reset(mail,token,newpass,confirmation,dialog,m_login,p_login);
                }
            }
        });
    }

    private void reset (final String email
            , String token
            , final String newpass
            , String confirm
            , final AlertDialog dialog
            , final EditText emaillogin
            , final EditText passwordlogin)
    {
        final ACProgressFlower acProgressFlower=new ApiManager().showProgressDialog(MainActivity.this,"Resetting");
        acProgressFlower.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("password",newpass);
        params.put("password_confirmation",confirm);
        params.put("token",token);
        Call<ResponseBody> call=userInterface.submitResetRequest(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    acProgressFlower.dismiss();
                    dialog.dismiss();
                    new ApiManager().showSuccessInActivityNotClose(MainActivity.this,"Reset successfully");
                    emaillogin.setText(email);
                    passwordlogin.setText(newpass);
                }
                else
                {
                    acProgressFlower.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new ApiManager().showError(MainActivity.this,"Sorry! "+jObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                acProgressFlower.dismiss();
                new ApiManager().showWarning(MainActivity.this,"Fail for "+t.getMessage());
            }
        });
    }

    private void sendMailToReset(String email,
                                 final TextInputLayout otp,
                                 final TextInputLayout password,
                                 final TextInputLayout confirm,
                                 final LinearLayout linearLayout1,
                                 final LinearLayout linearLayout2)
    {
        final ACProgressFlower dialog=new ApiManager().showProgressDialog(MainActivity.this,"Sending email");
        dialog.show();
        HashMap<String,String> param=new HashMap<>();
        param.put("email",email);
        Call<ResponseBody> call= userInterface.sendMailToReset(param);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    dialog.dismiss();
                    GoneAppearence(otp,
                            password,
                            confirm,
                            linearLayout2);
                    animatorline1(linearLayout1,1500);
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    dialog.dismiss();
                    new ApiManager().showError(MainActivity.this,"Your email's not been taken yet.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                new ApiManager().showWarning(MainActivity.this,"Failure for "+t.getMessage());
            }
        });
    }

    public void animatorline1 (View view1,long duration)
    {
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(view1,"y",650f);
        animatorY.setDuration(duration);
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator alphaAnimation=ObjectAnimator.ofFloat(view1,View.ALPHA,1.0f,0.0f);
        alphaAnimation.setDuration(duration);
        animatorSet.playTogether(animatorY,alphaAnimation);
        animatorSet.start();
    }
    public void animatorline1back (View view1,long duration)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator alphaAnimation=ObjectAnimator.ofFloat(view1,View.ALPHA,0.0f,1.0f);
        alphaAnimation.setDuration(duration);
        animatorSet.playTogether(alphaAnimation);
        animatorSet.start();
    }

    private boolean validEmail (TextInputLayout textInputLayout)
    {
        String s=textInputLayout.getEditText().getText().toString();
        if (s.isEmpty())
        {
            textInputLayout.setError("Field must not be empty");
            textInputLayout.setError("Field must not be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(s).matches())
        {
            textInputLayout.setError("Invalid email");
            textInputLayout.setHint("Invalid email");
            return false;
        }
        else {
            textInputLayout.setHint("Email is valid");
            textInputLayout.setError(null);
            return true;
        }
    }
    private boolean validNewPass (TextInputLayout textInputLayout)
    {
        String s=textInputLayout.getEditText().getText().toString();
        if (s.isEmpty())
        {
            textInputLayout.setError("Field must not be empty");
            textInputLayout.setHint("Field must not be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(s).matches())
        {
            textInputLayout.setError("Password has at least 6 characters");
            textInputLayout.setHint("Password has at least 6 characters");
            return false;
        }
        else {
            textInputLayout.setHint("New password is valid");
            textInputLayout.setError(null);
            return true;
        }
    }

    private boolean validOTP (TextInputLayout textInputLayout)
    {
        String s=textInputLayout.getEditText().getText().toString();
        if (s.isEmpty())
        {
            textInputLayout.setError("Field must not be empty");
            textInputLayout.setHint("Field must not be empty");
            return false;
        }
        else if (s.length()!=6)
        {
            textInputLayout.setError("Invalid code");
            textInputLayout.setHint("Code must be 6 characters");
            return false;
        }
        else {
            textInputLayout.setHint("Code is valid");
            textInputLayout.setError(null);
            return true;
        }
    }
    private boolean validRetype (TextInputLayout textInputLayout,String comp)
    {
        String s=textInputLayout.getEditText().getText().toString();
        if (s.isEmpty())
        {
            textInputLayout.setError("Field must not be empty");
            textInputLayout.setHint("Field must not be empty");
            return false;
        }
        else if (!s.equals(comp))
        {
            textInputLayout.setError("Not match");
            textInputLayout.setHint("Not match");
            return false;
        }
        else {
            textInputLayout.setHint("Retype correctly");
            textInputLayout.setError(null);
            return true;
        }

    }


    private void getInfoUser ()
    {

        tokenUser.setToken(token);
        tokenUser.setPassword(s_password);
        tokenUser.setEmail(email_sending);
        Call<ResponseBody>call =userInterface.getInfoUser("Bearer" + " " + token);
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
                        tokenUser.setUserOnline(User_cache);
                        tokenUser.setAddress(User_cache.getAddress());
                        // send User_cache to menu_activity

                        Thread thread=new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(500);
                                    Intent Successfull_intent = new Intent(MainActivity.this, menu_activity.class);
                                    startActivity(Successfull_intent);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Toast.makeText(MainActivity.this, "Token bad "+token, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Login ()
    {
        final String email = this.et_emailname.getText().toString().trim();
        final String password = this.et_pass.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("token");

                            // get token
                            token=message;
                            s_password=et_pass.getText().toString();
                            email_sending = email;
                            if(!message.isEmpty()) {
                                getInfoUser();
                                Toast.makeText(MainActivity.this, "Login Success! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Login fail "+error.toString(), Toast.LENGTH_SHORT).show();
                        openDialog ();
                        prog_login.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                return params;
        }

    };
        // set timeout for Volley response.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            6000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openDialog ()
    {
        new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Login fail!")
                .setContentText("That RENTaLINE account doesn't exist. \nWanna get new one ?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent register_intent=new Intent(getApplicationContext(),register.class);
                        Pair [] pairs =new Pair[2];
                        pairs[0]=new Pair<View,String>(logo_inLogin,"logoImage");
                        pairs[1]=new Pair<View,String>(btn_login,"btnTrans");
                        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                        startActivity(register_intent,activityOptions.toBundle());
                    }
                })
                .setCancelText("Try again")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void dummyCLick (View view)
    {
        // set visible for text click event
    }

}
