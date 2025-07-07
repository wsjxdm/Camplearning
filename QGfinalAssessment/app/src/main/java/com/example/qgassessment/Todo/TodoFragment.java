package com.example.qgassessment.Todo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qgassessment.R;
import com.example.qgassessment.Utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private ListView todoListView;
    private List<TodoBean> todos;
    private TodoAdapter adapter;
    private DatabaseHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        initViews(view);
        ImageButton writeButton = view.findViewById(R.id.todo_write);
        writeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TodoEditActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void initViews(View view) {
        todoListView = view.findViewById(R.id.todo_list);
        todos = new ArrayList<>();
        adapter = new TodoAdapter(getContext(), todos);
        todoListView.setAdapter(adapter);
        dbHelper = new DatabaseHelper(getContext());

        //编辑事项
        todoListView.setOnItemClickListener((parent, view1, position, id) -> {
            TodoBean todo = todos.get(position);
            Intent intent = new Intent(getActivity(), TodoEditActivity.class);
            intent.putExtra("todo_id", todo.getId());
            startActivity(intent);
        });

        //删除事项
        todoListView.setOnItemLongClickListener((parent, view1, position, id) -> {      //设置长按
            showDeleteDialog(position);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodos();
    }

    private void loadTodos() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int currentUserId = pref.getInt("current_user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        todos.clear();
        todos.addAll(dbHelper.getAllTodos(currentUserId));
        adapter.notifyDataSetChanged();
    }

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("删除事项")
                .setMessage("确定要删除这条事项吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    TodoBean todo = todos.get(position);
                    if (dbHelper.deleteTodo(todo.getId())) {
                        loadTodos();
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}