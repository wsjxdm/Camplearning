package com.example.qgassessment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.qgassessment.Account.AccountFragment;
import com.example.qgassessment.Finance.FinanceFragment;
import com.example.qgassessment.Todo.TodoFragment;
import com.example.qgassessment.Utils.ViewPagerAdapter;
import com.example.qgassessment.Weather.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainUI extends AppCompatActivity {


    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_ui);

        String accountId = getIntent().getStringExtra("id");    //用于接收从SignIn活动传过来的账户名称
        AccountFragment accountFragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", accountId);
        accountFragment.setArguments(bundle);

        viewPager = findViewById(R.id.main_view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherFragment());     //插入不同功能对应的Fragment
        adapter.addFragment(accountFragment);
        adapter.addFragment(new TodoFragment());
        adapter.addFragment(new FinanceFragment());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);        //为MainUI添加位于页面底部的指示器
        tabLayout.getTabAt(0).setText("天气");
        tabLayout.getTabAt(1).setText("用户");
        tabLayout.getTabAt(2).setText("每日事项");
        tabLayout.getTabAt(3).setText("记账本");

        viewPager.setCurrentItem(1);



    }
}