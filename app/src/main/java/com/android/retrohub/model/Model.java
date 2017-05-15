package com.android.retrohub.model;

import com.android.retrohub.model.data.GitToken;
import com.android.retrohub.model.data.UserInfo;
import com.android.retrohub.model.data.SearchReposList;
import com.android.retrohub.model.data.UserRepos;

import java.util.List;

import rx.Observable;

/**
 * Created by batsa on 25.04.2017.
 */

public interface Model {

    Observable<GitToken> getAccessToken(String code, String clientId, String clientSecret, String redirectUri);

    Observable<UserInfo> getUserInfo(String token);

    Observable<List<UserRepos>> getUserRepos(String token, String sort, String visibility);

    Observable<SearchReposList> getSearchRepos(String q, String sort, String order);
}
