package com.example.qgassessment.Finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qgassessment.R;

import java.util.List;

public class FinanceAdapter extends BaseAdapter {       //用于向ListView插入记录
    private Context context;
    private List<AccountBean> records;

    public FinanceAdapter(Context context, List<AccountBean> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
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
            view = inflater.inflate(R.layout.finance_record_item, parent, false);
        }

        AccountBean record = records.get(position);
        TextView typeView = view.findViewById(R.id.finance_item_type);
        TextView moneyView = view.findViewById(R.id.finance_item_money);
        TextView timeView = view.findViewById(R.id.finance_item_time);

        typeView.setText(record.getType());
        moneyView.setText(String.valueOf(record.getMoney()));
        timeView.setText(record.getTime());

        return view;
    }


}
