package com.kadiryaka.kalorimerkezi.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.adapter.DietListAdapter;
import com.kadiryaka.kalorimerkezi.dto.DietDTO;
import com.kadiryaka.kalorimerkezi.dto.DietListDTO;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DietActivity extends ActionBarActivity {
    ActionBarDrawerToggle slider;

    String token;

    private ListView dietListView;

    UserResource userResource = new UserResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_diet);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if(token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }

        dietListView = (ListView) findViewById(R.id.listDiet);

        fetchDiets();
    }

    private void fetchDiets() {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        userResource.getDiyetListByUser(token, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final DietListDTO dietListDTO = (DietListDTO) o;

                if(dietListDTO.diyetList.size() == 0) {
                    RelativeLayout notFoundError = (RelativeLayout) findViewById(R.id.notFoundError);
                    LinearLayout contentArea = (LinearLayout) findViewById(R.id.contentArea);

                    notFoundError.setVisibility(View.VISIBLE);
                    contentArea.setVisibility(View.GONE);
                } else {
                    DietListAdapter dietListAdapter = new DietListAdapter(DietActivity.this, dietListDTO.diyetList, dietListDTO.currentDiyet);
                    dietListView.setAdapter(dietListAdapter);

                    dietListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DietActivity.this);
                            alertDialogBuilder.setMessage(dietListDTO.diyetList.get(i).icerik).setCancelable(true);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                            textView.setTextSize(12);
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(DietActivity.this, error);
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
