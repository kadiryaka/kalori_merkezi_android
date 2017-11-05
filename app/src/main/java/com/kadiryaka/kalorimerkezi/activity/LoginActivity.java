package com.kadiryaka.kalorimerkezi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.dto.LoginRequest;
import com.kadiryaka.kalorimerkezi.dto.LoginResponse;
import com.kadiryaka.kalorimerkezi.resource.TokenResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.ObscuredSharedPreferences;
import com.kadiryaka.kalorimerkezi.util.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends ActionBarActivity {
    SharedPreferences prefs;

    TokenResource tokenResource = new TokenResource();

    private TextView loginFormTitle;
    private TextView loginUsernameTitle;
    private EditText loginUsernameInput;
    private TextView loginPasswordTitle;
    private EditText loginPasswordInput;
    private Button loginButton;

    Typeface font;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);

        prefs = new ObscuredSharedPreferences(this, this.getSharedPreferences(Constants.store_info_name, Context.MODE_PRIVATE));
        token = Utils.getUserToken(this);

        font = Utils.getFont(this);

        TextView title;
        if(findViewById(R.id.action_bar_title)!=null) {
            title = (TextView)findViewById(R.id.action_bar_title);
        } else {
            title = (TextView)findViewById(Utils.getVersionBasedTitleId(this));
        }
        title.setTypeface(font);
        title.setTextSize(Constants.title_font_size);

        loadComponent();
        applyFont();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin(
                        loginUsernameInput.getText().toString(),
                        loginPasswordInput.getText().toString()
                );
            }
        });

        if(token != null && !token.trim().equalsIgnoreCase(Constants.empty)) {
            goProfile();
        }
    }

    private void loadComponent() {
        loginFormTitle = (TextView)findViewById(R.id.loginFormTitle);
        loginUsernameTitle = (TextView)findViewById(R.id.loginUsernameTitle);
        loginUsernameInput = (EditText)findViewById(R.id.loginUsernameInput);
        loginPasswordTitle = (TextView)findViewById(R.id.loginPasswordTitle);
        loginPasswordInput = (EditText)findViewById(R.id.loginPasswordInput);
        loginButton = (Button)findViewById(R.id.loginButton);
    }

    private void applyFont() {
        loginFormTitle.setTypeface(font);
        loginUsernameTitle.setTypeface(font);
        loginUsernameInput.setTypeface(font);
        loginPasswordTitle.setTypeface(font);
        loginPasswordInput.setTypeface(font);
        loginButton.setTypeface(font);
    }

    private void doLogin(String username, String password) {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        tokenResource.login(loginRequest, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final LoginResponse loginResponse = (LoginResponse) o;

                if(loginResponse.getResult().equalsIgnoreCase("success")) {
                    prefs.edit().putString(Constants.store_token, loginResponse.getData()).commit();
                    prefs.edit().putString(Constants.store_username, loginResponse.getUsername()).commit();
                    prefs.edit().putString(Constants.store_surname, loginResponse.getSurname()).commit();
                    goProfile();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                if(error.getResponse() == null) {
                    Toast.makeText(LoginActivity.this, getString(R.string.wrong_system), Toast.LENGTH_SHORT).show();
                } else if(error.getResponse().getStatus() == 404) {
                    Toast.makeText(LoginActivity.this, getString(R.string.wrong_login), Toast.LENGTH_SHORT).show();
                    loginUsernameInput.setText(Constants.empty);
                    loginPasswordInput.setText(Constants.empty);
                    loginUsernameInput.setFocusable(true);
                    loginUsernameInput.requestFocus();
                } else {
                    Utils.showMessage(getString(R.string.error), error.getMessage(), LoginActivity.this);
                }
            }
        });
    }

    private void goProfile() {
        finish();
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
