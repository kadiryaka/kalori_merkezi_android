package com.kadiryaka.kalorimerkezi.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.adapter.MeasureNameListAdapter;
import com.kadiryaka.kalorimerkezi.dto.SizeDTO;
import com.kadiryaka.kalorimerkezi.dto.SizeListDTO;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MeasureActivity extends ActionBarActivity {

    ActionBarDrawerToggle slider;

    Spinner mSpinner;

    String token;

    UserResource userResource = new UserResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_measure);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if(token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }

        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        userResource.getUserSize(token, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final SizeListDTO sizeListDTO = (SizeListDTO) o;

                if(sizeListDTO.sizeList.size() == 0) {
                    RelativeLayout notFoundError = (RelativeLayout) findViewById(R.id.notFoundError);
                    ScrollView contentArea = (ScrollView) findViewById(R.id.contentArea);

                    notFoundError.setVisibility(View.VISIBLE);
                    contentArea.setVisibility(View.GONE);
                } else {
                    mSpinner = (Spinner) findViewById(R.id.names);
                    mSpinner.setAdapter(new MeasureNameListAdapter(MeasureActivity.this, R.layout.measure_names_item, sizeListDTO.sizeList));
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            SizeDTO sizeDTO = sizeListDTO.sizeList.get(i);

                            TextView tarih = (TextView) findViewById(R.id.tarihValue);
                            tarih.setText(sizeDTO.tarih);

                            TextView kilo = (TextView) findViewById(R.id.kiloValue);
                            kilo.setText(String.valueOf(sizeDTO.kilo) + " kg");

                            TextView omuz = (TextView) findViewById(R.id.omuzValue);
                            omuz.setText(String.valueOf(sizeDTO.omuz) + " cm");

                            TextView sagPazu = (TextView) findViewById(R.id.sagPazuValue);
                            sagPazu.setText(String.valueOf(sizeDTO.sag_pazu) + " cm");

                            TextView solPazu = (TextView) findViewById(R.id.solPazuValue);
                            solPazu.setText(String.valueOf(sizeDTO.sol_pazu) + " cm");

                            TextView gogus = (TextView) findViewById(R.id.gogusValue);
                            gogus.setText(String.valueOf(sizeDTO.gogus) + " cm");

                            TextView karin = (TextView) findViewById(R.id.karinValue);
                            karin.setText(String.valueOf(sizeDTO.karin) + " cm");

                            TextView bel = (TextView) findViewById(R.id.belValue);
                            bel.setText(String.valueOf(sizeDTO.bel) + " cm");

                            TextView kalca = (TextView) findViewById(R.id.kalcaValue);
                            kalca.setText(String.valueOf(sizeDTO.kalca) + " cm");

                            TextView sagBacak = (TextView) findViewById(R.id.sagBacakValue);
                            sagBacak.setText(String.valueOf(sizeDTO.sag_bacak) + " cm");

                            TextView solBacak = (TextView) findViewById(R.id.solBacakValue);
                            solBacak.setText(String.valueOf(sizeDTO.sol_bacak) + " cm");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(MeasureActivity.this, error);
            }
        });
    }

    private void goLogin() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (slider.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        slider.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        slider.onConfigurationChanged(newConfig);
    }
}
