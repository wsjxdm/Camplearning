package com.example.qgassessment.Sign;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;

public class SignUp extends AppCompatActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        Button transfer = (Button) findViewById(R.id.turn_to_sign_in);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignIn.class);
                startActivity(intent);
            }
        });

        showPassword();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Button signUp = (Button) findViewById(R.id.sign_up);
        accountEdit = (EditText) findViewById(R.id.edit_account);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(!account.isEmpty()&&!password.isEmpty()) {
                    if (dbHelper.checkUser(account,password)){
                        Toast.makeText(SignUp.this,"account is already created!"+"\n"+"please click sign in.", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("account", accountEdit.getText().toString().trim());
                        values.put("password", passwordEdit.getText().toString().trim());
                        db.insert("users", null, values);
                        Intent intent = new Intent(v.getContext(), JumpToSignIn.class);
                        startActivity(intent);
                    }
                } else if (accountEdit.getText().toString().isEmpty()&&!passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "account can't be empty", Toast.LENGTH_SHORT).show();
                } else if (!accountEdit.getText().toString().isEmpty()&&passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "password can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUp.this, "please enter account and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showPassword() {
        ImageButton showPasswordButton = findViewById(R.id.show_password_button);
        EditText passwordEdit = findViewById(R.id.edit_password);
        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPasswordButton.setImageResource(R.drawable.ic_eye_off);
                } else {
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPasswordButton.setImageResource(R.drawable.ic_eye_on);
                }
                passwordEdit.setSelection(passwordEdit.getText().length());
            }
        });
    }


}