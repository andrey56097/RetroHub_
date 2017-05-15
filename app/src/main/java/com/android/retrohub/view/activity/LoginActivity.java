package com.android.retrohub.view.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.retrohub.R;
import com.android.retrohub.presenter.LoginPresenter;
import com.android.retrohub.utils.Constants;
import com.android.retrohub.utils.SessionManager;
import com.android.retrohub.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.retrohub.utils.AndroidApp.hasNetwork;
import static com.android.retrohub.utils.Constants.API_BASE_URL;
import static com.android.retrohub.utils.Constants.SCOPE;

/**
 * Created by batsa on 25.04.2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_to_refresh_layout_login) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.webv) WebView web;
    @BindView(R.id.loadingViewLogin) ProgressBar loadingView;

    private LoginPresenter presenter;
    SessionManager session;
    private String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(this,this);
        session = new SessionManager(getApplicationContext());

        web.getSettings().setJavaScriptEnabled(true);
        if (hasNetwork()) {
            web.setWebViewClient(new LoginActivity.MyWebViewClient());
            web.loadUrl(API_BASE_URL
                    + "?redirect_uri="
                    + Constants.REDIRECT_URL
                    + "&response_type=code&client_id="
                    + Constants.CLIENT_ID
                    + SCOPE);
        } else {
            makeSnack(getResources().getString(R.string.string_internet_connection_error_and_swipe));
        }

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void makeSnack(String text) {
        Snackbar snackbar = Snackbar.make(loadingView, text, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public void showError(String error) {
        makeSnack(error);
    }

    @Override
    public void showProgressView() {
        loadingView.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideProgressView() {
        loadingView.setVisibility(android.view.View.GONE);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public String getAuthCode() {
        return authCode;
    }

    @Override
    public void onRefresh() {
        if (!hasNetwork()){
            makeSnack(getResources().getString(R.string.string_internet_connection_error));
        }
        web.setWebViewClient(new LoginActivity.MyWebViewClient());
        web.loadUrl(API_BASE_URL
                + "?redirect_uri="
                + Constants.REDIRECT_URL
                + "&response_type=code&client_id="
                + Constants.CLIENT_ID
                + SCOPE);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            web.stopLoading();
            if (!hasNetwork()) {
                makeSnack(getResources().getString(R.string.string_internet_connection_error_and_swipe));
            }
        }

        @Override
        public void onPageFinished(final WebView view, String url) {

            super.onPageFinished(view, url);

            swipeRefreshLayout.setRefreshing(false);

            if (url.contains("?code=")) {

                web.stopLoading();
                Uri uri = Uri.parse(url);
                authCode = uri.getQueryParameter("code");

                if (authCode != null) {
                    presenter.getData();
                }
            }
        }
    }
}


