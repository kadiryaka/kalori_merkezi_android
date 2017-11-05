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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.adapter.ExcersizeDateListAdapter;
import com.kadiryaka.kalorimerkezi.adapter.MeasureNameListAdapter;
import com.kadiryaka.kalorimerkezi.adapter.PersonelExcersizeListAdapter;
import com.kadiryaka.kalorimerkezi.dto.DateListAndEgzersizListDTO;
import com.kadiryaka.kalorimerkezi.dto.DoListDTO;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PersonalExcersizeActivity extends ActionBarActivity {
    UserResource userResource = new UserResource();

    ActionBarDrawerToggle slider;

    Spinner mSpinner;

    String token;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_personal_excersize);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if(token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }

        listView = (ListView) findViewById(R.id.listPersonalExcersize);

        fetchMainOp();
    }

    private void fetchMainOp() {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        userResource.dateListAndEgzersizList(token, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final DateListAndEgzersizListDTO dateListAndEgzersizListDTO = (DateListAndEgzersizListDTO) o;

                mSpinner = (Spinner) findViewById(R.id.dates);
                mSpinner.setAdapter(new ExcersizeDateListAdapter(PersonalExcersizeActivity.this, R.layout.excersize_dates_item, dateListAndEgzersizListDTO.dateFormatList));

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        fetchList(dateListAndEgzersizListDTO.dateList.get(i).tarih);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(PersonalExcersizeActivity.this, error);
            }
        });
    }

    private void fetchList(String date) {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        userResource.excersizListByDate(token, date, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final DoListDTO doListDTO = (DoListDTO) o;

                PersonelExcersizeListAdapter dietListAdapter = new PersonelExcersizeListAdapter(PersonalExcersizeActivity.this, doListDTO.egzersizList);
                listView.setAdapter(dietListAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(PersonalExcersizeActivity.this, error);
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
