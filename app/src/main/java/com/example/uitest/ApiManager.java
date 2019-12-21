package com.example.uitest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String baseURL="http://192.168.1.103:8000/";

    public static String getBaseURL() {
        return baseURL;
    }

    private  static final Retrofit.Builder builder= new Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
            .client(new ApiManager().okHttpClient());
    private static final Retrofit retrofit=builder.build();

    private static final UserInterface userInterface=retrofit.create(UserInterface.class);
    public static UserInterface getServiceUserInterface ()
    {
        return  userInterface;
    }

    // some sample dialog
    public void showSuccessInActivity (final Activity thisActivity,String whatSuccess)
    {
        new SweetAlertDialog(thisActivity,SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(whatSuccess)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        thisActivity.finish();
                    }
                })
                .show();
    }
    public void showSuccessInActivityNotClose (final Activity thisActivity,String whatSuccess)
    {
        new SweetAlertDialog(thisActivity,SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(whatSuccess)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }
    public void showWarning (final Context context, String whatWarning)
    {
        new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setContentText(whatWarning)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void showWarningClose (final Activity context, String whatWarning)
    {
        new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE)
                .setContentText(whatWarning)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        context.finish();
                    }
                })
                .show();
    }

    public void showError (final Context context, String whatError)
    {
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setContentText(whatError)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
    }

    public float getFloatFromPriceString( String price)
    {
        String newone=price.replace("VNĐ per day","");
        float floatPrice = Float.parseFloat(newone.trim());
        return floatPrice/1000;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public ACProgressFlower showProgressDialog (final Context context, String content)
    {
        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(content)
                .fadeColor(Color.DKGRAY)
                .textMarginTop(10)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    // set time out for retrofit
    public OkHttpClient okHttpClient ()
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        return  okHttpClient;
    }

}
