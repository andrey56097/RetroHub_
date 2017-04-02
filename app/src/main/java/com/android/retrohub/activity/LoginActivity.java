package com.android.retrohub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.retrohub.models.GetToken;
import com.android.retrohub.R;
import com.android.retrohub.api.ApiService;
import com.android.retrohub.api.RetroClient;
import com.android.retrohub.models.GitUser;
import com.android.retrohub.services.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String clientId = "500acef59d850d1cf45c";
    private final String clientSecret = "7b5dcf5e074b4c6c75016c64ffae9d7e491b0380";
    private final String redirectUri = "http://localhost";
    private final String grandType = "grant_type";
    private final String API_BASE_URL = "https://github.com/login/oauth/authorize";

    WebView web;
    private SwipeRefreshLayout swipeRefreshLayout;

    public String token;

    public static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);

        web = (WebView) findViewById(R.id.webv);
        web.getSettings().setJavaScriptEnabled(true);

        if (InternetConnection.ckeckConnection(getApplicationContext())) {
            web.setWebViewClient(new MyWebViewClient());
            web.loadUrl(API_BASE_URL
                    + "?redirect_uri="
                    + redirectUri
                    + "&response_type=code&client_id="
                    + clientId
                    +"&scope=repo_hook%20repo%20public_repo%20admin:public_key");
        } else {
            Snackbar.make(web, "Please check your internet connection and swipe down", Snackbar.LENGTH_LONG).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (InternetConnection.ckeckConnection(getApplicationContext())) {
                    web.loadUrl(API_BASE_URL + "?redirect_uri=" + redirectUri + "&response_type=code&client_id=" + clientId+"&scope=repo_hook%20repo%20public_repo%20admin:public_key ");
                } else {
                    Snackbar.make(web, "Please check your internet connection and swipe down", Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);

                sharedPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putBoolean("first", true);
                editor.commit();

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }

    ProgressDialog dialog;
    private class MyWebViewClient extends WebViewClient {

        private final String clientId = "500acef59d850d1cf45c";
        private final String clientSecret = "7b5dcf5e074b4c6c75016c64ffae9d7e491b0380";
        private final String redirectUri = "http://localhost";
        private final String grandType = "grant_type";
        private final String API_BASE_URL = "https://github.com/login/oauth/authorize";


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

//            web.loadUrl(API_BASE_URL + "?redirect_uri=" + redirectUri + "&response_type=code&client_id=" + clientId);
//            if (InternetConnection.ckeckConnection(getApplicationContext())) {
//                web.loadUrl(API_BASE_URL + "?redirect_uri=" + redirectUri + "&response_type=code&client_id=" + clientId);
//            } else {
//                Snackbar.make(view, "Please check your internet connection and try again", Snackbar.LENGTH_LONG).show();
//            }
        }

        String code;

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
//                web.loadUrl("http://ru.stackoverflow.com/questions/616287/%D0%9D%D0%B5-%D1%83%D0%B4%D0%B0%D0%BB%D0%BE%D1%81%D1%8C-%D0%BE%D1%82%D0%BA%D1%80%D1%8B%D1%82%D1%8C-%D0%B2%D0%B5%D0%B1-%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D1%83-webview");
            web.stopLoading();
//            Snackbar.make(view, "Please check your internet connection and try again", Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);

            boolean authComplete = false;


            /**
             * Progress Dialog for User Interaction
             */

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle(getString(R.string.string_getting_gson_title));
            dialog.setMessage(getString(R.string.string_getting_gson_massage));


            if (url.contains("?code=") && authComplete != true) {


                web.stopLoading();
//                dialog.show();

                Uri uri = Uri.parse(url);
                code = uri.getQueryParameter("code");

                if (code != null) {

                    Toast.makeText(getApplicationContext(), "Authorization Code is: " + code, Toast.LENGTH_SHORT).show();

                    final ApiService api = RetroClient.getApiService();

                    /**
                     * FIRST REQUEST to get token
                     * Calling JSON
                     */
                    Call<GetToken> call = api.getMyJSON(code, clientId, clientSecret, redirectUri, grandType);

                    call.enqueue(new Callback<GetToken>() {
                        @Override
                        public void onResponse(Call<GetToken> call, Response<GetToken> response) {
                            if (response.isSuccessful()) {
                                /**
                                 * Got Successfully
                                 */
                                token = response.body().getAccessToken();

                                Log.e("TOKEN", token);


                                /**
                                 *  Go to next token
                                 */
                                if (token != null) {

                                    new GetUserInfo().execute(token);


//                                    dialog.dismiss();


                                } else {
                                    Snackbar.make(view, "Don`t get token " + token, Snackbar.LENGTH_LONG).show();
                                }

                            } else {
                                Snackbar.make(view, "Something went wrong with response", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetToken> call, Throwable t) {

                        }


                    });


                }
            }
            super.onPageFinished(view, url);

        }

    }

//    static boolean ooo = true;

    private class GetUserInfo extends AsyncTask<String, String, String> {
        String userLogin2;
        String imageUrl;
        MainActivity data = new MainActivity();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             *   SECOND REQUEST to get user name and image
             */
            ApiService api2 = RetroClient.getApiServiceGIT();

            /**
             * Calling JSON
             */

            Call<GitUser> call2 = api2.getUser("token " + token);

            call2.enqueue(new Callback<GitUser>() {
                @Override
                public void onResponse(Call<GitUser> call, Response<GitUser> response) {
                    if (response.isSuccessful()) {


                        userLogin2 = response.body().getLogin();
                        imageUrl = response.body().getAvatarUrl();


                        sharedPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
                        editor=sharedPreferences.edit();
                        editor.putString("token", "token "+token);
                        editor.putString("login", userLogin2);
                        editor.putString("imageUrl", imageUrl);
                        editor.commit();

                        dialog.dismiss();


                        finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }



                }

                @Override
                public void onFailure(Call<GitUser> call, Throwable t) {

                }
            });


        }


        @Override
        protected String doInBackground(String... strings) {

            return null;
        }



    }
}
