package com.example.qgassessment.Finance;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.qgassessment.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FinanceRecordsActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);
        tabLayout = findViewById(R.id.records_tablayout);
        viewPager = findViewById(R.id.records_viewpager);
        initPager();
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

        private void initPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        ExpensesFragment expensesFragment = new ExpensesFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(expensesFragment);
        fragmentList.add(incomeFragment);
        RecordViewPagerAdapter recordViewPagerAdapter = new RecordViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(recordViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}