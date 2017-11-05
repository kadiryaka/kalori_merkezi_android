package com.kadiryaka.kalorimerkezi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.activity.DietActivity;
import com.kadiryaka.kalorimerkezi.activity.ExcersizeActivity;
import com.kadiryaka.kalorimerkezi.activity.LoginActivity;
import com.kadiryaka.kalorimerkezi.activity.MeasureActivity;
import com.kadiryaka.kalorimerkezi.activity.NewExcersizeActivity;
import com.kadiryaka.kalorimerkezi.activity.PersonalExcersizeActivity;
import com.kadiryaka.kalorimerkezi.activity.ProfileActivity;
import com.kadiryaka.kalorimerkezi.adapter.DrawerListAdapter;
import com.kadiryaka.kalorimerkezi.resource.TokenResource;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SliderMenu {

    private TextView title;

    private Activity activity;

    private TokenResource tokenResource = new TokenResource();

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private TextView mUsername;
    private RelativeLayout mProfileBox;

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    public SliderMenu(Activity activity) {
        this.activity = activity;
    }

    public ActionBarDrawerToggle doMenu() {
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        mNavItems.add(new NavItem("Diyetlerim", "Periyodik olarak eklenmiş diyetlerinizi görüntüler.", R.drawable.ic_action_half_important));
        mNavItems.add(new NavItem("Egzersiz Programı", "Yapacağınız çalışmaları yönetmenizi sağlar.", R.drawable.ic_action_accept));
        mNavItems.add(new NavItem("Egzersiz Girişi", "Manuel olarak egzersiz eklemenizi sağlar.", R.drawable.ic_action_new));
        mNavItems.add(new NavItem("Kişisel Egzersizler", "Manuel girdiğiniz egzersizleri takip edebilirsiniz.", R.drawable.ic_action_person));
        mNavItems.add(new NavItem("Ölçülerim", "Ölçülerinizi takip etmenizi sağlar.", R.drawable.ic_action_labels));
        mNavItems.add(new NavItem("Çıkış", "Güvenli şekilde çıkmanızı sağlar.", R.drawable.ic_action_remove));
        mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        mProfileBox = (RelativeLayout) activity.findViewById(R.id.profileBox);
        mUsername = (TextView) activity.findViewById(R.id.userName);
        mDrawerPane = (RelativeLayout) activity.findViewById(R.id.drawerPane);
        mDrawerList = (ListView) activity.findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(activity, mNavItems);
        mDrawerList.setAdapter(adapter);

        mProfileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doProfile();
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mNavItems.get(position).mTitle.equalsIgnoreCase("Çıkış")) {
                    doLogout();
                } else if(mNavItems.get(position).mTitle.equalsIgnoreCase("Ölçülerim")) {
                    doMeasure();
                } else if(mNavItems.get(position).mTitle.equalsIgnoreCase("Diyetlerim")) {
                    doDiet();
                } else if(mNavItems.get(position).mTitle.equalsIgnoreCase("Egzersiz Programı")) {
                    doExercise();
                } else if(mNavItems.get(position).mTitle.equalsIgnoreCase("Egzersiz Girişi")) {
                    doNewExercise();
                } else if(mNavItems.get(position).mTitle.equalsIgnoreCase("Kişisel Egzersizler")) {
                    doPersonalExercise();
                }
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mUsername.setText(Utils.getUsername(activity) + " " + Utils.getSurname(activity));

        return mDrawerToggle;
    }

    private void doLogout() {
        tokenResource.logout(Utils.getUserToken(activity), new Callback() {
            @Override
            public void success(Object o, Response response) {
                SharedPreferences prefs = new ObscuredSharedPreferences(activity, activity.getSharedPreferences(Constants.store_info_name, Context.MODE_PRIVATE));
                prefs.edit().putString(Constants.store_token, null).commit();
                prefs.edit().putString(Constants.store_username, null).commit();
                prefs.edit().putString(Constants.store_surname, null).commit();
                activity.finish();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void doProfile() {
        activity.finish();
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
    }

    private void doMeasure() {
        activity.finish();
        Intent intent = new Intent(activity, MeasureActivity.class);
        activity.startActivity(intent);
    }

    private void doDiet() {
        activity.finish();
        Intent intent = new Intent(activity, DietActivity.class);
        activity.startActivity(intent);
    }

    private void doExercise() {
        activity.finish();
        Intent intent = new Intent(activity, ExcersizeActivity.class);
        activity.startActivity(intent);
    }

    private void doNewExercise() {
        activity.finish();
        Intent intent = new Intent(activity, NewExcersizeActivity.class);
        activity.startActivity(intent);
    }

    private void doPersonalExercise() {
        activity.finish();
        Intent intent = new Intent(activity, PersonalExcersizeActivity.class);
        activity.startActivity(intent);
    }
}
