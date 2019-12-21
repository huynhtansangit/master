package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class loading_and_greeting extends AppCompatActivity {
ImageView imageView ;
AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_and_greeting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imageView=(ImageView)findViewById(R.id.iv_animotion);
        if (imageView==null)
            throw new AssertionError();
        imageView.setBackgroundResource(R.drawable.aninmotion_loading);
        animationDrawable=(AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
        Thread myThread =new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep (4000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
