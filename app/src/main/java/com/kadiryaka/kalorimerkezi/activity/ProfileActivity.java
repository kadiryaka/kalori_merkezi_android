package com.kadiryaka.kalorimerkezi.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.SizeListDTO;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileActivity extends ActionBarActivity {
    ActionBarDrawerToggle slider;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if(token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }
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
