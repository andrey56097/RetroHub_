package com.android.retrohub.model.api;


import android.nfc.Tag;
import android.util.Log;

import com.android.retrohub.utils.AndroidApp;
import com.android.retrohub.utils.Constants;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by batsa on 15.02.2017.
 */

public class ApiModule {

    /**
     * GET Retrofit Instance
     */
    private static Retrofit getRetrofitInstance(String url) {

        /**
         *  Http logging
         */
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(AndroidApp.getInstance().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;

        Cache cache = new Cache(httpCacheDirectory, cacheSize);


        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .addInterceptor(log)
                .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    /**
     * GET API Service for authorization
     */

    public static ApiInterface getApiService() {
        return getRetrofitInstance(Constants.ROOT_URL).create(ApiInterface.class);
    }

    /**
     * GET API Service for getting Repos of user
     */

    public static ApiInterface getApiServiceGIT() {
        return getRetrofitInstance(Constants.ROOT_URL_GIT).create(ApiInterface.class);
    }


    /**
     * This Interceptors is used for cache data
     */

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {

            int maxAge = 1;
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            return originalResponse;
        }
    };


    private static final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        Request request = chain.request();

        if (!AndroidApp.hasNetwork()) {

            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    };

}
