package com.example.uitest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dialog_edit_info extends AppCompatDialogFragment {
    public EditText et_addr,et_phone,et_editusername;
    public TextView tv_gender,tv_birthday;
    public RadioButton rbtnmale,rbtnFemale,rbtnOther;
    public RadioGroup radioGroup;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private EditDialogListener editDialogListener,editDialogListenerToActivity;
    private String token ;
    private String birthdayFormat;
    private TokenUser tokenUser;
    UserInterface userInterface=ApiManager.getServiceUserInterface();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.edit_dialog,null);
        findView(view);
        builder.setView(view).setTitle("Edit Infomation").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getActivity(), " "+token, Toast.LENGTH_SHORT).show();
                ApplyEdition ();
            }
        });

        tv_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                    getContext(),
                    android.R.style.Theme_DeviceDefault_Dialog,
                    onDateSetListener,
                    1990,1,1);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month+=1;
                Calendar cal=Calendar.getInstance();
                SimpleDateFormat formatter =new SimpleDateFormat("yyyy-mm-dd");
                String s_date=String.format("%d/%d/%d",day,month,year);
                try {
                    Date date=formatter.parse(year+"-"+month+"-"+day);
                    birthdayFormat=formatter.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (cal.get(Calendar.YEAR)-year<=10)
                {
                    tv_birthday.setText("Invalid age");
                    tv_birthday.setTextColor(Color.RED);
                }
                else {tv_birthday.setText(s_date);tv_birthday.setTextColor(Color.parseColor("#008B8B"));}
            }
        };
        return builder.create();
    }
    // to push data
private void findView (View view)
{
    tokenUser=(TokenUser)getActivity().getApplicationContext();
    token=tokenUser.getToken();
    tv_birthday=view.findViewById(R.id.tv_birthday);
    et_addr =view.findViewById(R.id.et_youraddress);
    et_phone=view.findViewById(R.id.et_phoneprofile);
    et_editusername=view.findViewById(R.id.et_editUsername);
    tv_gender =view.findViewById(R.id.tv_gender_dialog);
    rbtnmale =view.findViewById(R.id.rbtn_male);
    rbtnFemale =view.findViewById(R.id.rbtn_female);
    rbtnOther =view.findViewById(R.id.rbtn_other);
    radioGroup=view.findViewById(R.id.radiogroup);
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
            if(checkid==rbtnmale.getId())
            {
                tv_gender.setText("Male");
            }
            if(checkid==rbtnFemale.getId())
            {
                tv_gender.setText("Female");
            }
            if(checkid==rbtnOther.getId())
            {
                tv_gender.setText("3rd");
            }
        }
    });
}
    public void ApplyEdition ()
    {

        if (!et_phone.getText().toString().equals(""))
        {
            String phoneNumber =et_phone.getText().toString();
            editDialogListener.sendStringInput("phone",phoneNumber);
            editProfile(phoneNumber,token,"phone");
            Toast.makeText(getContext(), "Updated phonenumber successfull", Toast.LENGTH_SHORT).show();
            tokenUser.getUserOnline().setPhone(phoneNumber);
        }//Toast.makeText(getContext(), "null in phone", Toast.LENGTH_SHORT).show();
        if (!et_addr.getText().toString().equals(""))
        {
            String address=et_addr.getText().toString();
            editDialogListener.sendStringInput("address",address);
            editProfile(address,token,"address");
            tokenUser.setAddress(address);
            Toast.makeText(getContext(), "Updated address successfull", Toast.LENGTH_SHORT).show();
            tokenUser.getUserOnline().setAddress(address);
        }//Toast.makeText(getContext(), "null in address", Toast.LENGTH_SHORT).show();
        if (!et_editusername.getText().toString().equals(""))
        {
            String username=et_editusername.getText().toString();
            editDialogListener.sendStringInput("username",username);
            editDialogListenerToActivity.sendStringInput("username",username);
            editProfile(username,token,"name");
            Toast.makeText(getContext(), "Updated your name successfull", Toast.LENGTH_SHORT).show();
            tokenUser.getUserOnline().setName(username);
        }//Toast.makeText(getContext(), "null in name", Toast.LENGTH_SHORT).show();
        if (!tv_gender.getText().toString().equals(""))
        {
            String gender=tv_gender.getText().toString();
            editDialogListener.sendStringInput("gender",gender);
            editProfile(gender,token,"gender");
            Toast.makeText(getContext(), "Updated gender successfull", Toast.LENGTH_SHORT).show();
            tokenUser.getUserOnline().setGender(gender);
        }//Toast.makeText(getContext(), "null in gender", Toast.LENGTH_SHORT).show();
        if (!tv_birthday.getText().toString().equals(""))
        {
            String birthday=tv_birthday.getText().toString();
            editDialogListener.sendStringInput("birthday",birthday);
            editProfile(birthdayFormat,token,"birth_day");
            Toast.makeText(getContext(), "Updated birthday successfull "+birthdayFormat, Toast.LENGTH_SHORT).show();
            tokenUser.getUserOnline().setBirth_day(birthday);
        }//Toast.makeText(getContext(), "null in birthday", Toast.LENGTH_SHORT).show();

    }

    private void editProfile(String editInfo, String tokenAuth, final String key)
    {
        HashMap<String,Object> map=new HashMap<>();
        map.put(key,editInfo);
        Call<ResponseBody> post=userInterface.EditProfile("Bearer"+" "+tokenAuth,map);
        post.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            editDialogListener=(EditDialogListener) getTargetFragment();    // send to fragment
            editDialogListenerToActivity=(EditDialogListener)getActivity(); // send to activity
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
