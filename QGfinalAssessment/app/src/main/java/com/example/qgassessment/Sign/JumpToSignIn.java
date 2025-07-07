package com.example.qgassessment.Sign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.qgassessment.R;

public class JumpToSignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jump_to_sign_in);
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(JumpToSignIn.this, SignIn.class);
                startActivity(intent);
                finish();           // 结束当前Activity
            }
        };

        handler.postDelayed(runnable,2000);     // 使用Handler的postDelayed方法延迟执行Runnable对象
    }
}