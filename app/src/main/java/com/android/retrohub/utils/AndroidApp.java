package com.android.retrohub.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by batsa on 13.05.2017.
 */

public class AndroidApp extends Application {
    private static AndroidApp instance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;
    }

    public static AndroidApp getInstance ()
    {
        return instance;
    }

    /**
     * CHECK WHETHER INTERNET CONNECTION AVAILABLE OR NOT
     */
    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
