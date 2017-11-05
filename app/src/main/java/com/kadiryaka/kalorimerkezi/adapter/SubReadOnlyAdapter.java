package com.kadiryaka.kalorimerkezi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kadiryaka.kalorimerkezi.activity.ExcersizeActivity;

public class SubReadOnlyAdapter extends ArrayAdapter<String> {
    public SubReadOnlyAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
    }

    public SubReadOnlyAdapter(ExcersizeActivity excersizeActivity, int select_dialog_item) {
        super(excersizeActivity, select_dialog_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextSize(12);

        return view;
    }
}
