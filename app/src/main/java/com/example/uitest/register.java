package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class register extends AppCompatActivity {
    private ImageButton Ibtn_back;
    private Button  btn_Create;
    private EditText et_yourname,et_password,et_email,et_password_confirm;
    private TextView tv_create_account;
    private ProgressBar pro_register;
    private TextInputLayout til_name,til_email,til_pass,til_passconfirm;
    private UserInterface userInterface=ApiManager.getServiceUserInterface();
    private ApiManager apiManager=new ApiManager();
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
private static String apiRegister ="api/register";
private static String URL_REGIST = ApiManager.getBaseURL()+apiRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        findView();
        Ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                // make effect for changing activity
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmInput(view)) {
                    //hide soft input window
                    apiManager.hideKeyboard(register.this);
                    btn_Create.setVisibility(View.GONE);
                    pro_register.setVisibility(View.VISIBLE);
                    RegistByRetrofit ();
                }
                else return;
            }
        });
        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(register.this, "Create account", Toast.LENGTH_SHORT).show();
            }
        });

    }
private  void findView ()
{
    Ibtn_back=(ImageButton)findViewById(R.id.Ibtn_back);
    btn_Create=(Button)findViewById(R.id.btn_create);
    et_yourname=(EditText)findViewById(R.id.et_register_name);
    et_email=(EditText)findViewById(R.id.et_regis_email);
    et_password=(EditText)findViewById(R.id.et_regis_password);
    et_password_confirm=(EditText)findViewById(R.id.et_regis_password_confirm);
    tv_create_account=(TextView)findViewById(R.id.tv_CreateAccount);
    til_pass=findViewById(R.id.tv_regis_password);
    til_name=findViewById(R.id.tv_regis_name);
    til_email=findViewById(R.id.tv_regis_email);
    til_passconfirm=findViewById(R.id.tv_regis_password_confirm);
    pro_register=findViewById(R.id.progress_bar_register);
}

    private boolean validateEmail() {
        String emailInput = til_email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            til_email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            til_email.setError("Please enter a valid email address");
            return false;
        } else {
            til_email.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = til_name.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            til_name.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 30) {
            til_name.setError("Username too long");
            return false;
        } else {
            til_name.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = til_pass.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            til_pass.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            til_pass.setError("Password too weak");
            Toast.makeText(this, "Password has no space\nAt least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            til_pass.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String password_confirmInput = et_password_confirm.getText().toString().trim();
        if (password_confirmInput.isEmpty()) {
            til_passconfirm.setError("Field can't be empty");
            return false;
        } else if (!password_confirmInput.equals(et_password.getText().toString().trim())) {
            til_passconfirm.setError("Not match");
            return false;
        } else {
            til_passconfirm.setError(null);
            return true;
        }
    }
    private boolean confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword() |!validateConfirmPassword()) {
            return false;}
            else return true;
        }

    private void RegistByRetrofit ()
    {
        HashMap<String, String> params =new HashMap<>();
        params.put("name", et_yourname.getText().toString().trim());
        params.put("email", et_email.getText().toString().trim());
        params.put("password", et_password.getText().toString().trim());
        params.put("password_confirmation", et_password_confirm.getText().toString().trim());
        Call<ResponseBody> call=userInterface.Register(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    btn_Create.setVisibility(View.VISIBLE);
                    pro_register.setVisibility(View.GONE);
                    String message = "";
                    try {
                        message =response.body().string();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(register.this, message, Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(register.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("We e-mailed for you. Please check mail to activate your account")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    register.this.finish();
                                }
                            })
                            .show();
                }
                else {
                    btn_Create.setVisibility(View.VISIBLE);
                    pro_register.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response.errorBody().string());
                        apiManager.showError(register.this,jsonObject.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btn_Create.setVisibility(View.VISIBLE);
                pro_register.setVisibility(View.GONE);
                apiManager.showWarning(register.this,"Failure for "+t.getMessage());
            }
        });
    }

    void close ()
    {
        this.finish();
    }
    public void dummyCLick (View view)
    {
        // set visible for text click event
    }
}
