package com.kadiryaka.kalorimerkezi.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;

import retrofit.RetrofitError;

public class Utils {
    public static void showMessage(String header, String content, Context context) {
        AlertDialog alertMessage = new AlertDialog.Builder(context).create();
        alertMessage.setTitle(header);
        alertMessage.setMessage(content);
        alertMessage.show();
    }

    public static Typeface getFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), Constants.font_path);
    }

    public static int getVersionBasedTitleId(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return context.getResources().getIdentifier("action_bar_title", "id", "android");
        } else {
            return R.id.action_bar_title;
        }
    }

    public static String getUserToken(Context context) {
        SharedPreferences prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(Constants.store_info_name, Context.MODE_PRIVATE));

        return prefs.getString(Constants.store_token, null);
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(Constants.store_info_name, Context.MODE_PRIVATE));

        return prefs.getString(Constants.store_username, null);
    }

    public static String getSurname(Context context) {
        SharedPreferences prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(Constants.store_info_name, Context.MODE_PRIVATE));

        return prefs.getString(Constants.store_surname, null);
    }

    public static void showRetrofitError(Context context, RetrofitError error) {
        if(error.getResponse() == null) {
            Toast.makeText(context, context.getString(R.string.wrong_system), Toast.LENGTH_SHORT).show();
        } else {
            Utils.showMessage(context.getString(R.string.error), error.getMessage(), context);
        }
    }
}