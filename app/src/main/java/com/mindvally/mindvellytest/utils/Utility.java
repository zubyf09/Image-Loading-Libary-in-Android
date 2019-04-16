package com.mindvally.mindvellytest.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;


public class Utility {


    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                return true;
            }
        }

        return false;
    }


    public static void showErrorSnackBar(View view, String message) {
        try {
            Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("", null);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(Color.WHITE);
            TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.BLACK);
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}




