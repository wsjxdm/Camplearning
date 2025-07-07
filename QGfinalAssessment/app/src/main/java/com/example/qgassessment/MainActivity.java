package com.example.qgassessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;


import com.example.qgassessment.Sign.SignIn;
import com.example.qgassessment.Sign.SignUp;
import com.example.qgassessment.Utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        Button enterApp = (Button) findViewById(R.id.enter);
        enterApp.setOnClickListener(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
                System.exit(0);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId()==R.id.enter){
            dbHelper.getWritableDatabase();
            intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        }
    }
}