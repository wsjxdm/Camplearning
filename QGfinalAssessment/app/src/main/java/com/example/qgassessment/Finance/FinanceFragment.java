package com.example.qgassessment.Finance;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FinanceFragment extends Fragment {

    private ListView recordListView;
    private TextView totalView, incomeView, expenseView;
    private List<AccountBean> records;
    private FinanceAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);

        initViews(view);

        ImageButton recordWrite = (ImageButton) view.findViewById(R.id.finance_record_write);
        recordWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FinanceRecordsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initViews(View view) {
        //初始化ListView和Adapter
        recordListView = view.findViewById(R.id.record_list);
        records = new ArrayList<>();
        adapter = new FinanceAdapter(getContext(), records);
        recordListView.setAdapter(adapter);

        //初始化统计信息的TextView
        totalView = view.findViewById(R.id.total_num);
        incomeView = view.findViewById(R.id.income_num);
        expenseView = view.findViewById(R.id.expense_num);

        //初始化数据库帮助类
        dbHelper = new DatabaseHelper(getContext());

        //设置ListView的长按删除功能
        recordListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //检查视图是否已初始化
        if (totalView != null && incomeView != null && expenseView != null) {
            loadRecords();
        }
    }

    private void loadRecords() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int currentUserId = pref.getInt("current_user_id", -1);
        //清空并重新加载记录
        records.clear();
        records.addAll(dbHelper.getAllRecords(currentUserId));
        //确保adapter不为空
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        //更新统计信息前检查视图是否存在
        if (totalView != null && incomeView != null && expenseView != null) {
            float income = dbHelper.getTotalIncome(currentUserId);
            float expense = dbHelper.getTotalExpense(currentUserId);
            float total = income - expense;

            totalView.setText(String.format("￥%.2f", total));
            incomeView.setText(String.format("￥%.2f", income));
            expenseView.setText(String.format("￥%.2f", expense));
        }
    }

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("删除记录")
                .setMessage("确定要删除这条记录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    AccountBean record = records.get(position);
                    if (dbHelper.deleteFinanceRecord(record.getId())) {
                        loadRecords();
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清空视图引用
        totalView = null;
        incomeView = null;
        expenseView = null;
        recordListView = null;
        adapter = null;
    }


}
