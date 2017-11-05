package com.kadiryaka.kalorimerkezi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.DietDTO;
import com.kadiryaka.kalorimerkezi.dto.ExcersizeDTO;

import java.util.List;

public class ExcersizeListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ExcersizeDTO> excersizeList;
    private Context context;
    private int currentExcersize;

    public ExcersizeListAdapter(Activity activity, List<ExcersizeDTO> excersizeList, int currentExcersize) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.excersizeList = excersizeList;
        this.context = activity.getBaseContext();
        this.currentExcersize = currentExcersize;
    }

    @Override
    public int getCount() {
        return excersizeList.size();
    }

    @Override
    public ExcersizeDTO getItem(int i) {
        return excersizeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.row_excersize_list, null);

        if(i % 2 != 0) {
            rowView.setBackgroundColor(Color.parseColor("#72BFC3"));
        }

        TextView excersizeTitle = (TextView) rowView.findViewById(R.id.excersizeTitle);
        TextView excersizeBaslangic = (TextView) rowView.findViewById(R.id.excersizeBaslangic);
        TextView excersizeBitis = (TextView) rowView.findViewById(R.id.excersizeBitis);
        TextView excersizeDurum = (TextView) rowView.findViewById(R.id.excersizeDurum);
        GradientDrawable shape = (GradientDrawable) excersizeDurum.getBackground();

        ExcersizeDTO excersizeDTO = excersizeList.get(i);

        excersizeTitle.setText(excersizeDTO.temp_adi);
        excersizeBaslangic.setText(excersizeDTO.bas_tarihi);
        excersizeBitis.setText(excersizeDTO.bitis_tarihi);

        if(currentExcersize == 0 && i == 0) {
            shape.setColor(Color.parseColor("#1aae88"));
        } else {
            shape.setColor(Color.parseColor("#e04959"));
        }
        setBc(excersizeDurum, shape);

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
