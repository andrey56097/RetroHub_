package com.android.retrohub.model.api;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;

import com.android.retrohub.activity.MainActivity;
import com.android.retrohub.utils.InternetConnection;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by batsa on 15.02.2017.
 */

public class RetroClient {

    /**
     * URLS
     */
    private static final String ROOT_URL = "https://github.com/";
    private static final String ROOT_URL_GIT = "https://api.github.com/";

    /**
     * GET Retrofit Instance
     */

    private static Retrofit getRetrofitInstance(String url) {

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//        httpClient.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
//        //setup cache
//
//        File httpCacheDirectory = new File(System.getProperty("java.io.tmpdir"), "responses");
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//
//
//        Cache cache = new Cache(httpCacheDirectory, cacheSize);
//
//        httpClient.cache(cache);


        httpClient.addInterceptor(log);

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


    /**
     * GET API Service for authorization
     */

    public static ApiService getApiService() {
        return getRetrofitInstance(ROOT_URL).create(ApiService.class);
    }

    /**
     * GET API Service for getting Repos of user
     */

    public static ApiService getApiServiceGIT() {
        return getRetrofitInstance(ROOT_URL_GIT).create(ApiService.class);
    }


    /**
     * This Interseptor is used for cache data
     */
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (Context.CONNECTIVITY_SERVICE != null ) {
                int maxAge = 5; // read from cache for 1 minute
//                int maxAge = 60 * 60 * 24 * 28;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }

    };
}
