package com.example.qgassessment.Todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qgassessment.R;

import java.util.List;

public class TodoAdapter extends BaseAdapter {
    private Context context;
    private List<TodoBean> todoList;

    public TodoAdapter(Context context, List<TodoBean> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.todo_item, parent, false);
        }

        TodoBean todo = todoList.get(position);
        TextView nameView = view.findViewById(R.id.todo_name);
        TextView dateView = view.findViewById(R.id.todo_date);

        nameView.setText(todo.getTitle());
        dateView.setText(todo.getStartTime());

        return view;
    }
}
