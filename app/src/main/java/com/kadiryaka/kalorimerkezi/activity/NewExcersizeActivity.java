package com.kadiryaka.kalorimerkezi.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.EgzDTO;
import com.kadiryaka.kalorimerkezi.dto.EgzListDTO;
import com.kadiryaka.kalorimerkezi.dto.SaveEgzDTO;
import com.kadiryaka.kalorimerkezi.resource.ServicesResource;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.service.ServicesService;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NewExcersizeActivity extends ActionBarActivity {
    ServicesResource servicesResource = new ServicesResource();
    UserResource userResource = new UserResource();

    EgzListDTO egzListDTO;

    ActionBarDrawerToggle slider;

    String token;

    Spinner mEgzId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_new_excersize);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if(token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }

        Button mCancel = (Button) findViewById(R.id.cancelButton);
        Button mNew = (Button) findViewById(R.id.newButton);
        mEgzId = (Spinner) findViewById(R.id.egz_id);
        final EditText mAgirlik = (EditText) findViewById(R.id.agirlik);
        final EditText mMakinaNo = (EditText) findViewById(R.id.makina_no);
        final EditText mAdet = (EditText) findViewById(R.id.adet);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAgirlik.setText("");
                mMakinaNo.setText("");
                mAdet.setText("");
                mAgirlik.requestFocus();
            }
        });

        mNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAgirlik.getText().toString().trim().equalsIgnoreCase("") ||
                   mMakinaNo.getText().toString().trim().equalsIgnoreCase("") ||
                   mAdet.getText().toString().trim().equalsIgnoreCase("")) {
                   Toast.makeText(NewExcersizeActivity.this, "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
                } else {
                    setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);

                    SaveEgzDTO saveEgzDTO = new SaveEgzDTO();
                    saveEgzDTO.egz_id = egzListDTO.egzersizList.get(mEgzId.getSelectedItemPosition()).egz_id;
                    saveEgzDTO.adet = mAdet.getText().toString();
                    saveEgzDTO.agirlik = Integer.valueOf(mAgirlik.getText().toString());
                    saveEgzDTO.makina_no = mMakinaNo.getText().toString();

                    userResource.saveOtherExcercize(token, saveEgzDTO, new Callback() {
                        @Override
                        public void success(Object o, Response response) {
                            setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                            mAgirlik.setText("");
                            mMakinaNo.setText("");
                            mAdet.setText("");
                            mAgirlik.requestFocus();

                            Toast.makeText(NewExcersizeActivity.this, "Başarıyla kaydedildi", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                            Utils.showRetrofitError(NewExcersizeActivity.this, error);
                        }
                    });
                }
            }
        });

        doSpinnerFetch();
    }

    public void doSpinnerFetch() {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        servicesResource.getAllExcersize(token, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                ArrayList<String> spinnerArray = new ArrayList<String>();

                egzListDTO = (EgzListDTO) o;
                for (EgzDTO egzDTO : egzListDTO.egzersizList) {
                    spinnerArray.add(egzDTO.egz_ad);
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(NewExcersizeActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mEgzId.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(NewExcersizeActivity.this, error);
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
