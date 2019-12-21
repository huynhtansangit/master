package com.example.uitest;

import com.example.uitest.comment.commentObj;
import com.example.uitest.comment.cmt_apdapter;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Post extends AppCompatActivity {
    public List<ItemObj> Items;
    private ItemObj itembyId;
    private String id_item;
    private TokenUser tokenUser;
    private  UserInterface userInterface;
    private ImageView imageView;
    private TextView title ;
    private TextView vhcName ;
    private TextView price ;
    private Button rentBtn;
    private TextView product ;
    private TextView NoSeat ;
    private TextView postViews ;
    private EditText cmtInput;
    private TextView postDetail ;
    private ListView reviewList;
    private ImageButton imBtn_delete;
    private int REQUEST_PHONE_CALL =1;
    List<commentObj> cmtList;
    // View dialog
    TextView tv_address,tv_birthday,tv_gender,tv_phone,tv_nameinProfileFrag;
    ImageView avt,wallpaper;
    Button btnCancel,btnContact;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        findView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent intent=getIntent();
        id_item =intent.getStringExtra("id");
        tokenUser=(TokenUser)getApplicationContext();
        userInterface=ApiManager.getServiceUserInterface();
        //Toast.makeText(this, ""+id_item, Toast.LENGTH_SHORT).show();
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color_toobar));
        /*ListView listView=findViewById(R.id.reviewList);
        CustomAdapter adapter=new CustomAdapter(this,R.layout.vertical_itemview, Items);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);*/
        getDetailsProduct(id_item);
        imBtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookDialog();
            }
        });

        reviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        findViewById(R.id.CmtBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCmtisNotNull(cmtInput))
                {
                    Comment(cmtInput);
                }
            }
        });
    }

    public boolean checkCmtisNotNull (EditText cmtInput)
    {
        String check=cmtInput.getText().toString();
        if (check.isEmpty())
        {
            cmtInput.setError("Field must be not empty");
            return false;
        }
        else if(check.length()>120)
        {
            cmtInput.setError("Too long. Less than 120 characters");
            return false;
        }
        else {
            cmtInput.setError(null);
            return true;
        }
    }

    private void Comment(final EditText editText)
    {
        final String idpost=title.getTag().toString();
        final String user_id=postDetail.getTag().toString();
        final String body=editText.getText().toString();
        HashMap<String,String> map=new HashMap<>();
        map.put("body",body);
        Call<ResponseBody> call=userInterface.postCmt("Bearer" + " " + tokenUser.getToken(),idpost,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    commentObj obj=new commentObj(
                            user_id,
                            tokenUser.getUserOnline().getName(),
                            tokenUser.getUserOnline().getAvatar(),idpost,body);
                    cmtList.add(0,obj);
                    reviewList.setAdapter(new cmt_apdapter(Post.this,R.layout.comment_layout,cmtList));
                    editText.setText(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new ApiManager().showWarning(Post.this,t.getMessage());
            }
        });
    }

    public void loadImageintoWallWithLink (String link,final RelativeLayout relativeLayout ,View view)
    {
        Glide.with(view).load(link).apply(RequestOptions.fitCenterTransform()).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                relativeLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });
    }
    void openBookDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(Post.this);
        LayoutInflater inflater=getLayoutInflater();
        final View view= inflater.inflate(R.layout.book_dialog,null);
        builder.setView(view);
        tv_address= view.findViewById(R.id.tv_youraddress);
        tv_birthday= view.findViewById(R.id.tv_birthday);
        tv_gender= view.findViewById(R.id.tv_gender);
        tv_phone= view.findViewById(R.id.tv_yourphone);
        avt=view.findViewById(R.id.im_Avt_profile);
        btnContact=view.findViewById(R.id.btn_contactdialog);
        tv_nameinProfileFrag=view.findViewById(R.id.tv_fragYourname);
        btnCancel=view.findViewById(R.id.btn_Cancel);
        relativeLayout=view.findViewById(R.id.relativelayout);
        //linearLayout.setAlpha(0.7f);

        ///////////////////////////////////////
        Call<Current_user_cache> call= userInterface.getUserbyId("Bearer" + " " + tokenUser.getToken(),postDetail.getTag().toString());
        call.enqueue(new Callback<Current_user_cache>() {
            @Override
            public void onResponse(Call<Current_user_cache> call, Response<Current_user_cache> response) {
                if (response.isSuccessful())
                {
                    tv_nameinProfileFrag.setText(response.body().getName());
                    tv_gender.setText(response.body().getGender());
                    tv_address.setText(response.body().getAddress());
                    tv_phone.setText(response.body().getPhone());
                    loadImgCircle(response.body().getAvatar(),avt);
                    tv_birthday.setText(response.body().getBirth_day());
                    loadImageintoWallWithLink(response.body().getWallpaper(),relativeLayout,view);
                }
            }

            @Override
            public void onFailure(Call<Current_user_cache> call, Throwable t) {

            }
        });
        //////////////////////////////////
        final AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call
                if(ContextCompat.checkSelfPermission(Post.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Post.this,Manifest.permission.CALL_PHONE))
                    {
                        new AlertDialog.Builder(Post.this)
                            .setTitle("Permission needed")
                            .setMessage("Need permisson to make a call")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(Post.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    }
                    else {
                        ActivityCompat.requestPermissions(Post.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_phone.getText()));// Initiates the Intent
                    startActivity(intent);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void findView(){
    //    <!---hình của bài viết - Imageview - id=postImg -->
    //<!---tiêu đề bài viết - textview - id= postTitle -->
    //<!---Tên phương tiện - textview - id= Vhc_name-->
    //<!---Giá thuê - textview - id= postPrice -->
    //<!---button đặt chỗ - button - id = rent_btn-->
    //<!---Nhà sản xuất - textview - id = product -->
    //<!---Số ghế - textview - id = NumOfSeat -->
    //<!---Lượt xem - textview - id = view -->
    //<!---Post detail - TextView - id = postDetail-->
    //<!---Review list - ListView - id = reviewList -->
         cmtInput=findViewById(R.id.cmtInput);
         imageView=findViewById(R.id.postImg);
         title =findViewById(R.id.postTitle);
         vhcName =findViewById(R.id.Vhc_name);
         price =findViewById(R.id.postPrice);
         rentBtn=findViewById(R.id.rent_btn);
         product =findViewById(R.id.product);
         NoSeat =findViewById(R.id.NumOfSeat);
         postViews =findViewById(R.id.view);
         postDetail =findViewById(R.id.postDetail);
         reviewList=findViewById(R.id.reviewList);
         imBtn_delete=findViewById(R.id.btn_delete);
         findViewById(R.id.ImgBtn_backArrow).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dummyClick();
             }
         });
         cmtList=new ArrayList<>();
    }
    public void GetList(){
        Items = new ArrayList<ItemObj>();
    }
    private void delete ()
    {
        Call<ResponseBody> call=userInterface.deleteitem("Bearer" + " " + tokenUser.getToken(),id_item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                        /*Toast.makeText(Post.this, "Delete successfully ", Toast.LENGTH_SHORT).show();
                        dummyClick();*/
                        new ApiManager().showSuccessInActivity(Post.this,"Deleted successfully");
                }
                else new ApiManager().showError(Post.this,"Not your own post");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Post.this, "Erorrrrrrrr", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }


        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    void loadImg (String path, ImageView imageView)
    {
        Glide.with(this).load(path).centerCrop().into(imageView);
    }
    void loadImgCircle (String path, ImageView imageView)
    {
        Glide.with(this).load(path).circleCrop().into(imageView);
    }
    public void getDetailsProduct (String id)
    {
        Call<ItemObj> call= userInterface.getDetailProduct("Bearer" + " " + tokenUser.getToken(),id);
        call.enqueue(new Callback<ItemObj>() {
            @Override
            public void onResponse(Call<ItemObj> call, Response<ItemObj> response) {
                vhcName.setText(response.body().getName());
                if(response.isSuccessful())
                {
                    float cost= new ApiManager().getFloatFromPriceString(response.body().getCost());
                    price.setText(cost+" K");
                    loadImg(response.body().getUrl_image(),imageView);
                    product.setText(response.body().getProducer());
                    NoSeat.setText(response.body().getSeats()+" seats");
                    postDetail.setText(response.body().getDetail());
                    // tag is user id
                    postDetail.setTag(response.body().getUser_id());
                    title.setText(response.body().getTitle());
                    //tag is post id
                    title.setTag(response.body().getId());
                    cmtList.addAll(response.body().getComments());
                    reviewList.setAdapter(new cmt_apdapter(Post.this,R.layout.comment_layout,cmtList));
                }
                else {
                    try {
                        new ApiManager().showWarning(Post.this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemObj> call, Throwable t) {
                new ApiManager().showWarning(Post.this,t.getMessage());
            }
        });
    }
    public void dummyClick ()
    {
        this.finish();
    }
}
