package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPost extends AppCompatActivity {

    public EditText title, name, price, detail, producer, seat;
    public TextView filepath, unitCurrency;
    public Button PickFile, Upload;
    private Uri pathImg;
    Intent intent;
    UserInterface userInterface =ApiManager.getServiceUserInterface();
    TokenUser tokenUser;
    InputMethodManager inputManager;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[!@#$%^&()+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    //".{6,}" +               //at least 4 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        findView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        PickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(confirmValidated())
                {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    post();
                }
                else
                {
                    Toast.makeText(UploadPost.this, "Need fill out all", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void dummyCLick (View view)
    {
        this.finish();
    }
    public  boolean validtitle ()
    {
        String stitle=title.getText().toString();
        if (stitle.isEmpty())
        {
            title.setError("Field must not be empty");
            return false;
        }
        return true;
    }
    public  boolean validname ()
    {
        String sname=name.getText().toString();
        if (sname.isEmpty())
        {
            name.setError("Field must not be empty");
            return false;
        }
        return true;
    }
    public  boolean validprice ()
    {
        String sprice=price.getText().toString();
        if (sprice.isEmpty())
        {
            price.setError("Field must not be empty");
            return false;
        }
        else if (!sprice.matches("\\d+(?:\\.\\d+)?"))
        {
            price.setError("Field must be number");
            return false;
        }
        return true;
    }
    public  boolean validProducer ()
    {
        String sprod=producer.getText().toString();
        if (sprod.isEmpty())
        {
            producer.setError("Field must not be empty");
            return false;
        }
        return true;
    }
    public  boolean validSeat ()
    {
        String sseat=seat.getText().toString();
        if (sseat.isEmpty())
        {
            seat.setError("Field must not be empty");
            return false;
        }
        return true;
    }
    boolean confirmValidated ()
    {
        if (!validname()|!validprice()|!validProducer()|!validSeat()|!validtitle()|!validPhoto())
        {
            return false;
        }
        return true;
    }

    boolean validPhoto ()
    {
        if (filepath.getText().toString().equals("null"))
        {
            filepath.setError("Must pick an image");
            return false;
        }
        return true;
    }
    void findView ()
    {
        tokenUser=(TokenUser)getApplicationContext();
        title=findViewById(R.id.Up_Title);
        name=findViewById(R.id.Up_name);
        price=findViewById(R.id.Up_Price);
        detail=findViewById(R.id.Up_detail);
        price=findViewById(R.id.Up_Price);
        seat=findViewById(R.id.Up_NoSeat);
        producer=findViewById(R.id.Up_Producer);
        filepath=findViewById(R.id.Up_filePath);
        PickFile=findViewById(R.id.Up_pickFile);
        Upload=findViewById(R.id.Up_SubmitBtn);
        unitCurrency=findViewById(R.id.tv_currencyUnit);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
            okhttp3.MultipartBody.FORM, descriptionString);
    }
    void post ()
    {
            File originalFile=new File(FileUtil.getPath(pathImg,this));
            RequestBody filePart=RequestBody.create(
                MediaType.parse(getContentResolver().getType(pathImg)),
                originalFile );
            MultipartBody.Part file =MultipartBody.Part.createFormData("photo",originalFile.getName(),filePart);
            RequestBody res_title=createPartFromString(title.getText().toString());
            RequestBody res_name=createPartFromString(name.getText().toString());
            RequestBody res_price=createPartFromString(price.getText().toString());
            RequestBody res_producer=createPartFromString(producer.getText().toString());
            RequestBody res_seat=createPartFromString(seat.getText().toString());
            RequestBody res_detail;
        if(!detail.getText().toString().isEmpty())
        {
            res_detail=createPartFromString(detail.getText().toString());
        }
        else res_detail=createPartFromString(" ");

        HashMap<String,RequestBody> map =new HashMap<>();
        map.put("title",res_title);
        map.put("name",res_name);
        map.put("cost",res_price);
        map.put("producer",res_producer);
        if(!detail.getText().toString().isEmpty())
        {
            map.put("detail",res_detail);
        }
        else map.put("detail",res_detail);
        map.put("seats",res_seat);
        Call<ResponseBody>call= userInterface.PostNewItem("Bearer" + " " + tokenUser.getToken(),map,file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    new ApiManager().showSuccessInActivity(UploadPost.this,"Posted successfully");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadPost.this, "Failure "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10&&resultCode== Activity.RESULT_OK){
            pathImg =data.getData();
            filepath.setText(pathImg.toString());
        }
    }
    public void close()
    {
        this.finish();
    }
}
