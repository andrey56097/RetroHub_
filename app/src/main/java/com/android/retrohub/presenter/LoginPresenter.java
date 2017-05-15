package com.android.retrohub.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.android.retrohub.R;
import com.android.retrohub.model.Model;
import com.android.retrohub.model.ModelImpl;
import com.android.retrohub.model.data.GitToken;
import com.android.retrohub.model.data.UserInfo;
import com.android.retrohub.utils.SessionManager;
import com.android.retrohub.view.LoginView;

import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.android.retrohub.utils.AndroidApp.hasNetwork;
import static com.android.retrohub.utils.Constants.CLIENT_ID;
import static com.android.retrohub.utils.Constants.CLIENT_SECRET;
import static com.android.retrohub.utils.Constants.REDIRECT_URL;

/**
 * Created by batsa on 25.04.2017.
 */

public class LoginPresenter implements BasePresenter {

    private Model model = new ModelImpl();
    private LoginView view;

    private Subscription subscription = Subscriptions.empty();
    private SessionManager session;

    private Context context;

    public LoginPresenter(LoginView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getData() {

        if (hasNetwork()) {
            session = new SessionManager(context);

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }

            view.showProgressView();
            subscription = model.getAccessToken(view.getAuthCode(), CLIENT_ID, CLIENT_SECRET, REDIRECT_URL)
                    .subscribe(new Observer<GitToken>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(Resources.getSystem().getString(R.string.string_error));
                        }

                        @Override
                        public void onNext(GitToken getToken) {

                            session.saveToken(getToken.getAccessToken());
                            SharedPreferences sharedPreferences = context.getSharedPreferences("AndroidHivePref", Context.MODE_PRIVATE);

                            model.getUserInfo("token " + sharedPreferences.getString("token", "token"))
                                    .subscribe(new Observer<UserInfo>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            view.showError(Resources.getSystem().getString(R.string.string_error));
                                        }

                                        @Override
                                        public void onNext(UserInfo gitUser) {

                                            if (view == null)
                                                return;
                                            if (gitUser != null) {
                                                view.hideProgressView();
                                                session.createLoginSession(gitUser.getLogin(), gitUser.getAvatarUrl());
                                                session.goToMain();
                                                view.closeActivity();
                                            } else {
                                            }
                                        }
                                    });
                        }
                    });
        } else view.showError(Resources.getSystem().getString(R.string.string_internet_connection_error));
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
