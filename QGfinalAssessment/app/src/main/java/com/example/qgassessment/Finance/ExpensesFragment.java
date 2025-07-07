package com.example.qgassessment.Finance;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;

import java.util.Calendar;


public class ExpensesFragment extends Fragment {

    EditText recordType,recordMoney;
    TextView recordTime;
    private Calendar calendar;
    private Button saveButton;
    private AccountBean accountBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        calendar = Calendar.getInstance();   //Calendar用于再添加记录页面更改时间

        initView(view);
        accountBean = new AccountBean();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());   //获取当前登录的用户account
        int currentUserId = pref.getInt("current_user_id", -1);
        accountBean.setUserId(currentUserId);

        recordTime.setText("点击选择时间");       //为添加记录页面的TextView设置文字
        setupTimePickerListener();      //选择时间

        return view;
    }

    private void setupTimePickerListener() {
        recordTime.setOnClickListener(v -> showDateTimePicker());       //监听TextView的点击事件
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // 显示时间选择器
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                updateTimeDisplay();
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateTimeDisplay() {
        String timeStr = String.format("%d年%d月%d日 %02d:%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        recordTime.setText(timeStr);

        //更新AccountBean
        accountBean.setTime(timeStr);
        accountBean.setYear(calendar.get(Calendar.YEAR));
        accountBean.setMonth(calendar.get(Calendar.MONTH) + 1);
        accountBean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void initView(View view) {

            //初始化输入框和时间显示
            recordType = view.findViewById(R.id.record_expenses_type);
            recordMoney = view.findViewById(R.id.records_expenses_money);
            recordTime = view.findViewById(R.id.records_expenses_date);

            //初始化保存按钮
            saveButton = view.findViewById(R.id.expenses_ensure);
            if (saveButton != null) {
                saveButton.setOnClickListener(v -> saveRecord());
            }

    }

    private void saveRecord() {
        try {
            String type = recordType.getText().toString();
            String moneyStr = recordMoney.getText().toString();

            if (TextUtils.isEmpty(type) || TextUtils.isEmpty(moneyStr)) {
                Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            //获取当前用户ID
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            int currentUserId = pref.getInt("current_user_id", -1);

            if (currentUserId == -1) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                return;
            }

            float money = -Float.parseFloat(moneyStr);  //支出用负数
            accountBean.setType(type);
            accountBean.setMoney(money);
            accountBean.setUserId(currentUserId);

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            long result = dbHelper.insertFinanceRecord(accountBean);

            if (result != -1) {
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                // 清空输入
                recordType.setText("");
                recordMoney.setText("");
                // 关闭当前Activity
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "请输入有效的金额", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("ExpensesFragment", "Error saving record: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getContext(), "保存失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
