package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class menu_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,EditDialogListener {
    private ImageView imageAvatar;
    private TextView tv_navi_name,tv_navi_email;
    private NavigationView navigationView;
    private LinearLayout wallpaper_Llayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    // Fragment
    private Profile_fragment profileFragment;
    private DashBoard_Activity dashBoardActivity;
    // variable to get path avt and background
    public Uri pathAvt;
    public Intent getPathIntent;
    public Uri pathProfileBackground;
    //
    private Current_user_cache User_cache;
    private String token,email;
    private Bitmap bitmap;
    UserInterface userInterface=ApiManager.getServiceUserInterface();
    TokenUser currentToken;
    private int STORAGE_PERMISSION_CODE=1;
    // receive data which just has changed from dialog_edit_info
    @Override
    public void sendStringInput(String key, String message) {
        if(key=="username")
        {
            tv_navi_name.setText(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent successfull_login =getIntent();

        new themes().loadThemes(menu_activity.this);

        navigationView=(NavigationView)findViewById(R.id.navigattionview);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        wallpaper_Llayout=(LinearLayout)headerView.findViewById(R.id.linearLayout) ;
        drawerLayout =(DrawerLayout)findViewById(R.id.Drawer_layout) ;
        imageAvatar=(ImageView)headerView.findViewById(R.id.im_avatar);
        tv_navi_name=(TextView)headerView.findViewById(R.id.navigation_username);
        tv_navi_email=(TextView)headerView.findViewById(R.id.navigation_email);
        currentToken=(TokenUser)getApplicationContext();
        token = currentToken.getToken();
        email=currentToken.getEmail();
        dashBoardActivity=new DashBoard_Activity();
        // initiate action toolbar;
        initActionBar();
        if(ContextCompat.checkSelfPermission(menu_activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) 
        {
            Toast.makeText(this, "You granted permission", Toast.LENGTH_SHORT).show();
        }
        else {
            requestStoragePermission ();
        }
        // picture for testing
        getInfoUser (successfull_login);
        //Create fragments
        if (savedInstanceState==null)
        {
            Bundle bundle=new Bundle();
            bundle.putString("token",token);
            profileFragment=new Profile_fragment();
            profileFragment.setArguments(bundle);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,profileFragment,"Profile");
            transaction.commit();
            navigationView.setCheckedItem(R.id.item_profile);
        }
        // evevnt click to change avt image
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(menu_activity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                    getPathIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getPathIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(getPathIntent, "Select picture"), 10);
                }
                else {
                    requestStoragePermission ();
                }
            }
        });
        // and here is to change background
        wallpaper_Llayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(menu_activity.this);
                builder.setTitle("Confirmation! Ask for sure")
                        .setMessage("Would you like to change another background ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getPathIntent=new Intent(Intent.ACTION_GET_CONTENT);
                                getPathIntent.setType("image/*");
                                startActivityForResult(Intent.createChooser(getPathIntent,"Select picture"),11 );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //

    }
    // initialization
    private void init (Intent successfull_login)
    {
        //User_cache=(Current_user_cache)successfull_login.getSerializableExtra("User_cache");
        tv_navi_name.setText(User_cache.getName());
        tv_navi_email.setText(email);
        loadImageintoAvatar(User_cache.getAvatar(),imageAvatar);
        if (User_cache.getWallpaper()==null)
        {
            loadImageintoWallDefault(wallpaper_Llayout);
        }else loadImageintoWallWithLink(User_cache.getWallpaper(),wallpaper_Llayout);
    }

    private void requestStoragePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(menu_activity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("Need permisson to pick image")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(menu_activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(menu_activity.this, "Application's not allowed to read your storage", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        logout();
                    }
                }).create().show();
        }
        else {
            ActivityCompat.requestPermissions(menu_activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    // load hình địa chỉ ảnh trên internet lên tường
    private void loadImageintoDrawer (String link,final DrawerLayout drawerLayout )
    {
        Glide.with(this).load(link).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                drawerLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });
    }
    public void loadImageintoWallWithLink (String link,final LinearLayout linearLayout )
    {
        Glide.with(this).load(link).apply(RequestOptions.fitCenterTransform()).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                linearLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });
    }
    private void loadImageintoWallDefault (final LinearLayout linearLayout)
    {
        Glide.with(this).load(R.mipmap.wallpaper_default).apply(RequestOptions.fitCenterTransform()).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                linearLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });
    }
    // load a path of image to make avatar
    private void loadImageintoAvatar (String link,final ImageView imageView )
    {
        Glide.with(this).load(link).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public void dummyCLick(View view) {
    }
    private void initActionBar()
    {
        Toolbar toolbar= (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    //implement onNavigationItemSeleted
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.item_Dashboard)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,dashBoardActivity,"Dashboard");
            transaction.commit();

        }
        if(id==R.id.item_Contact)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,new contact_fragment(),"Contact");
            transaction.commit();
        }
        if(id==R.id.item_Help)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,new support_fragment(),"Help");
            transaction.commit();
        }
        if(id==R.id.item_profile)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,profileFragment,"Profile");
            transaction.commit();
        }
        if(id==R.id.item_Logout)
        {
            logout();
        }
        if(id==R.id.item_themes)
        {
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.include_fragment,new themes(),"themes");
            transaction.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    public  void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    // get path image
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 11:
                if (resultCode==RESULT_OK)
                    if(data!=null){
                        pathProfileBackground=data.getData();
                        try {
                            loadImageintoWallWithLink(pathProfileBackground.toString(),wallpaper_Llayout);
                            UploadFile(pathProfileBackground,token,"wallpaperImg");
                            currentToken.getUserOnline().setWallpaper(pathProfileBackground.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                break;
            case 10:
                if(resultCode==RESULT_OK){
                    pathAvt=data.getData();
                    // save uri of image into database.
                    try {
                        loadImageintoAvatar(pathAvt.toString(),imageAvatar);
                        UploadFile(pathAvt,token,"avatarImg");
                        loadImageintoAvatar(pathAvt.toString(),(ImageView)profileFragment.getView().findViewById(R.id.im_Avt_profile));
                        currentToken.getUserOnline().setAvatar(pathAvt.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }
    private String encodeingImagetoString (Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] imgBytes= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    private void UploadFile (Uri ImgUri, String authToken, final String key)
    {
        File originalFile=new File(FileUtil.getPath(ImgUri,this));
        RequestBody filePart=RequestBody.create(
            MediaType.parse(getContentResolver().getType(ImgUri)),
            originalFile );
            MultipartBody.Part file =MultipartBody.Part.createFormData(key,originalFile.getName(),filePart);
        Call<ResponseBody> post=userInterface.UploadImage("Bearer"+" "+authToken,file);
        post.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    showSuccessDialognotCloseActivity(menu_activity.this,"Update " + key + " successfully");
                }
                else {
                    try {
                        Toast.makeText(menu_activity.this, "Something Wrong  "+response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorDialognotCloseActivity(menu_activity.this,"Failure in updating "+t.getMessage());
            }
        });
    }
public void showSuccessDialognotCloseActivity (Activity thisActivity, String content) {
    SweetAlertDialog dialog = new SweetAlertDialog(thisActivity, SweetAlertDialog.SUCCESS_TYPE)
            .setContentText(content)
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
}
    public void showErrorDialognotCloseActivity (Activity thisActivity, String content) {
        SweetAlertDialog dialog = new SweetAlertDialog(thisActivity, SweetAlertDialog.ERROR_TYPE)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }



    private void getInfoUser (final Intent successfull_login)
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
                        init(successfull_login);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Toast.makeText(menu_activity.this, "Token bad "+token, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(menu_activity.this, "Failure !", Toast.LENGTH_SHORT).show();
            }
        });
    }


public void logout ()
{
   Call<ResponseBody>call= userInterface.Logout("Bearer" + " " + token);
   call.enqueue(new Callback<ResponseBody>() {
       @Override
       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
           try {
               if(response.body() !=null)
               {
                   Toast.makeText(menu_activity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
                   finish();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       @Override
       public void onFailure(Call<ResponseBody> call, Throwable t) {
           Toast.makeText(menu_activity.this, "Sorry! "+t.toString(), Toast.LENGTH_SHORT).show();
       }
   });
}


}
