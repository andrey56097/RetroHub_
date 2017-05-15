package com.android.retrohub.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.android.retrohub.R;
import com.android.retrohub.model.Model;
import com.android.retrohub.model.ModelImpl;
import com.android.retrohub.model.data.UserRepos;
import com.android.retrohub.view.MainView;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.android.retrohub.utils.AndroidApp.hasNetwork;
import static com.android.retrohub.utils.Constants.SORT_REPO;
import static com.android.retrohub.utils.Constants.VISABILITY;

/**
 * Created by batsa on 26.04.2017.
 */

public class MainPresenter implements BasePresenter {

    private Model model = new ModelImpl();
    private MainView view;
    private Subscription subscription = Subscriptions.empty();

    private Context context;

    public MainPresenter(MainView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getData() {
        if (!hasNetwork()){
            view.showError(context.getString(R.string.string_internet_connection_error));
        }
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            view.showProgressView();
            SharedPreferences sharedPreferences = context.getSharedPreferences("AndroidHivePref", Context.MODE_PRIVATE);
            subscription = model.getUserRepos("token " + sharedPreferences.getString("token", "token"), SORT_REPO, VISABILITY)
                    .subscribe(new Subscriber<List<UserRepos>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(List<UserRepos> userReposes) {
                            if (view == null)
                                return;
                            view.hideProgressView();
                            if (userReposes != null && !userReposes.isEmpty()) {
                                view.showData(userReposes);
                            } else {
                                view.showEmptyList();
                            }
                        }
                    });
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
