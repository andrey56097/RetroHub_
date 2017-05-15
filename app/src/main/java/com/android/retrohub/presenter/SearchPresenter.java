package com.android.retrohub.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.android.retrohub.R;
import com.android.retrohub.model.Model;
import com.android.retrohub.model.ModelImpl;
import com.android.retrohub.model.data.SearchReposList;
import com.android.retrohub.view.SearchView;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.android.retrohub.utils.AndroidApp.hasNetwork;
import static com.android.retrohub.utils.Constants.ORDER;
import static com.android.retrohub.utils.Constants.SORT;


/**
 * Created by batsa on 26.04.2017.
 */

public class SearchPresenter implements BasePresenter {

    private Model model = new ModelImpl();
    private SearchView view;
    private Subscription subscription = Subscriptions.empty();

    private Context context;

    public SearchPresenter(SearchView view, Context context) {
        this.view = view;
        this.context = context;
    }
    @Override
    public void getData() {
        if (hasNetwork()) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            view.showProgressView();

            subscription = model.getSearchRepos(view.getSearchTerm(), SORT, ORDER)
                    .subscribe(new Subscriber<SearchReposList>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(Resources.getSystem().getString(R.string.string_error));
                        }

                        @Override
                        public void onNext(SearchReposList searchReposList) {
                            if (view == null)
                                return;
                            view.hideProgressView();
                            if (searchReposList != null && !searchReposList.getItems().isEmpty()) {
                                view.showData(searchReposList.getItems());
                            } else {
                                view.showEmptyList();
                            }
                        }
                    });
        } else  view.showError(context.getString(R.string.string_internet_connection_error));
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
