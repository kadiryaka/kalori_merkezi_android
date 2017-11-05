package com.kadiryaka.kalorimerkezi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.SizeDTO;

import java.util.List;

public class MeasureNameListAdapter extends ArrayAdapter<List<SizeDTO>> {

    Context context;
    List<SizeDTO> objects;

    public MeasureNameListAdapter(Context context, int textViewResourceId,
                                  List objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.measure_names_item, parent, false);
        TextView label = (TextView)row.findViewById(R.id.name);
        label.setText(objects.get(position).baslik);

        ImageView icon = (ImageView)row.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.ic_action_sort_by_size);

        return row;
    }
}