package com.example.qgassessment.Sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qgassessment.MainActivity;
import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;
import com.example.qgassessment.MainUI;

public class SignIn extends AppCompatActivity {

    private DatabaseHelper db;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox passwordRem;
    private boolean isPasswordVisible = false;

    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        rememberPassword();
        showPassword();
        db = new DatabaseHelper(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
    public void rememberPassword(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        passwordRem = (CheckBox) findViewById(R.id.passRem);
        accountEdit = (EditText) findViewById(R.id.edit_account);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        boolean isRemember = pref.getBoolean("remember_password",false);
        signIn = (Button) findViewById(R.id.sign_in);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            passwordRem.setChecked(true);
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (!accountEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString().isEmpty()) {
                    if (db.checkUser(account, password)) {
                        editor = pref.edit();
                        if (passwordRem.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", account);
                            editor.putString("password", password);
                        } else {
                            editor.clear();
                        }
                        //保存当前登录账户
                        int userId = db.getUserId(account);
                        editor.putInt("current_user_id", userId);
                        editor.apply();
                        Intent intent = new Intent(SignIn.this, MainUI.class);
                        intent.putExtra("id",account);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignIn.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                    }
                } else if (accountEdit.getText().toString().isEmpty()&&!passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(SignIn.this, "account can't be empty", Toast.LENGTH_SHORT).show();
                } else if (!accountEdit.getText().toString().isEmpty()&&passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(SignIn.this, "password can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignIn.this, "please enter account and password", Toast.LENGTH_SHORT).show();
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