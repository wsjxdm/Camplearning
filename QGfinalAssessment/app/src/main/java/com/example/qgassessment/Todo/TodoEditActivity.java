package com.example.qgassessment.Todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;

import java.util.Calendar;

public class TodoEditActivity extends AppCompatActivity {

    private EditText titleEdit, startTimeEdit, endTimeEdit, contentEdit;
    private Button saveButton;
    private ImageButton backButton;
    private Calendar startCalendar, endCalendar;
    private DatabaseHelper dbHelper;
    private TodoBean currentTodo;
    private int todoId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_edit);
        initViews();
        initCalendars();
        //获取传入的todoId，判断是新建还是编辑
        todoId = getIntent().getIntExtra("todo_id", -1);
        if (todoId != -1) {
            //如果是编辑模式，加载现有的todo数据
            loadTodoData();
        }

        setupListeners();
    }

    private void initViews() {
        titleEdit = findViewById(R.id.todo_edit_title);
        startTimeEdit = findViewById(R.id.todo_edit_start);
        endTimeEdit = findViewById(R.id.todo_edit_end);
        contentEdit = findViewById(R.id.todo_edit_content);
        saveButton = findViewById(R.id.todo_edit_save);
        backButton = findViewById(R.id.todo_edit_back);
        dbHelper = new DatabaseHelper(this);

        //设置时间选择框不可手动输入
        startTimeEdit.setFocusable(false);
        endTimeEdit.setFocusable(false);
    }

    private void initCalendars() {
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);
    }

    private void loadTodoData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int currentUserId = pref.getInt("current_user_id", -1);

        if (currentUserId != -1) {
            currentTodo = dbHelper.getTodoById(todoId, currentUserId);
            if (currentTodo != null) {
                //填充现有数据
                titleEdit.setText(currentTodo.getTitle());
                startTimeEdit.setText(currentTodo.getStartTime());
                endTimeEdit.setText(currentTodo.getEndTime());
                contentEdit.setText(currentTodo.getContent());

                //解析时间到Calendar对象
                parseTimeToCalendar(currentTodo.getStartTime(), startCalendar);
                parseTimeToCalendar(currentTodo.getEndTime(), endCalendar);
            }
        }
    }

    private void setupListeners() {     //注册监听器
        //返回按钮点击事件
        backButton.setOnClickListener(v -> finish());
        //开始时间选择
        startTimeEdit.setOnClickListener(v -> showDateTimePicker(startCalendar, startTimeEdit));
        //结束时间选择
        endTimeEdit.setOnClickListener(v -> showDateTimePicker(endCalendar, endTimeEdit));
        //保存按钮点击事件
        saveButton.setOnClickListener(v -> saveTodo());
    }

    private void showDateTimePicker(final Calendar calendar, final EditText editText) {
        //日期选择器
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //时间选择器
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                updateTimeDisplay(calendar, editText);
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

    private void updateTimeDisplay(Calendar calendar, EditText editText) {
        String timeStr = String.format("%d年%d月%d日 %02d:%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        editText.setText(timeStr);
    }

    private void parseTimeToCalendar(String timeStr, Calendar calendar) {
        String[] dateParts = timeStr.split("年|月|日\\s+|:");
        calendar.set(Calendar.YEAR, Integer.parseInt(dateParts[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateParts[3]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(dateParts[4]));
    }

    private void saveTodo() {
        String title = titleEdit.getText().toString().trim();
        String startTime = startTimeEdit.getText().toString().trim();
        String endTime = endTimeEdit.getText().toString().trim();
        String content = contentEdit.getText().toString().trim();
        //数据验证
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入事项标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(startTime)) {
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endCalendar.before(startCalendar)) {
            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取当前用户ID
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int currentUserId = pref.getInt("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        //准备保存的数据
        TodoBean todo = todoId == -1 ? new TodoBean() : currentTodo;
        todo.setTitle(title);
        todo.setStartTime(startTime);
        todo.setEndTime(endTime);
        todo.setContent(content);
        todo.setUserId(currentUserId);

        boolean success;
        if (todoId == -1) {
            //新建todo
            success = dbHelper.insertTodo(todo) != -1;
        } else {
            //更新todo
            success = dbHelper.updateTodo(todo);
        }

        if (success) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}