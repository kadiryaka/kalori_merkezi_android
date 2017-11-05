package com.kadiryaka.kalorimerkezi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.DietDTO;

import java.util.List;

public class DietListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<DietDTO> dietList;
    private Context context;
    private int currentDiyet;

    public DietListAdapter(Activity activity, List<DietDTO> dietList, int currentDiyet) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dietList = dietList;
        this.context = activity.getBaseContext();
        this.currentDiyet = currentDiyet;
    }

    @Override
    public int getCount() {
        return dietList.size();
    }

    @Override
    public DietDTO getItem(int i) {
        return dietList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.row_diet_list, null);

        if(i % 2 != 0) {
            rowView.setBackgroundColor(Color.parseColor("#72BFC3"));
        }

        TextView textDietTitle = (TextView) rowView.findViewById(R.id.dietTitle);
        TextView textDietBaslangic = (TextView) rowView.findViewById(R.id.dietBaslangic);
        TextView textDietBitis = (TextView) rowView.findViewById(R.id.dietBitis);
        TextView textDietDurum = (TextView) rowView.findViewById(R.id.dietDurum);
        GradientDrawable shape = (GradientDrawable) textDietDurum.getBackground();

        DietDTO dietDTO = dietList.get(i);

        textDietTitle.setText(dietDTO.temp_adi);
        textDietBaslangic.setText(dietDTO.bas_tarihi);
        textDietBitis.setText(dietDTO.bitis_tarihi);

        if(currentDiyet == 0 && i == 0) {
            shape.setColor(Color.parseColor("#1aae88"));
        } else {
            shape.setColor(Color.parseColor("#e04959"));
        }
        setBc(textDietDurum, shape);

        return rowView;
    }

    private void setBc(TextView textView, GradientDrawable gradientDrawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackgroundDrawable(gradientDrawable);
        } else {
            textView.setBackground(gradientDrawable);
        }
    }
}
