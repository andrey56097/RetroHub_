package com.android.retrohub.model;

import com.android.retrohub.model.api.ApiInterface;
import com.android.retrohub.model.api.ApiModule;
import com.android.retrohub.model.data.GitToken;
import com.android.retrohub.model.data.UserInfo;
import com.android.retrohub.model.data.SearchReposList;
import com.android.retrohub.model.data.UserRepos;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by batsa on 25.04.2017.
 */

public class ModelImpl implements Model{

    ApiInterface api = ApiModule.getApiService();
    ApiInterface api2 = ApiModule.getApiServiceGIT();

    @Override
    public Observable<GitToken> getAccessToken(String code, String clientId, String clientSecret, String redirectUri) {
        return api.getAccessToken(code,clientId,clientSecret,redirectUri)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserInfo> getUserInfo(String token) {
        return api2.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserRepos>> getUserRepos(String token, String sort, String visibility) {
        return api2.getUserRepos(token,sort,visibility)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SearchReposList> getSearchRepos(String q, String sort, String order) {
        return api2.getSearchRepos(q,sort,order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
