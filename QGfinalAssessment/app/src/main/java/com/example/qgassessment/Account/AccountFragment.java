package com.example.qgassessment.Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qgassessment.MainUI;
import com.example.qgassessment.R;
import com.example.qgassessment.Sign.SignUp;

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        String ID = getArguments() != null ? getArguments().getString("id") : "未登录";    //获取MainUI传过来的的账户名称
        TextView showID = view.findViewById(R.id.welcome_account);
        showID.setText(ID);

        Button exit = (Button) view.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLoggedIn = false;
                Intent intent1 = new Intent(getActivity(), MainUI.class);
                intent1.putExtra("Loggin",isLoggedIn);
                Intent intent2 = new Intent(getActivity(), SignUp.class);
                startActivity(intent1);
                startActivity(intent2);
            }
        });
        return view;
    }
}