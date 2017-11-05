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
import com.kadiryaka.kalorimerkezi.dto.DoDTO;
import com.kadiryaka.kalorimerkezi.dto.DoStrDTO;

import java.util.List;

public class PersonelExcersizeListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<DoStrDTO> list;
    private Context context;

    public PersonelExcersizeListAdapter(Activity activity, List<DoStrDTO> list) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.context = activity.getBaseContext();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DoStrDTO getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.row_personel_excersize_list, null);

        if(i % 2 != 0) {
            rowView.setBackgroundColor(Color.parseColor("#72BFC3"));
        }

        TextView egz_id = (TextView) rowView.findViewById(R.id.egz_id);
        TextView makina_no = (TextView) rowView.findViewById(R.id.makina_no);
        TextView agirlik = (TextView) rowView.findViewById(R.id.agirlik);
        TextView adet = (TextView) rowView.findViewById(R.id.adet);

        DoStrDTO doDTO = list.get(i);

        egz_id.setText(doDTO.egz_ad);
        makina_no.setText(doDTO.makina_no);
        agirlik.setText(String.valueOf(doDTO.agirlik));
        adet.setText(doDTO.adet);

        return rowView;
    }
}
