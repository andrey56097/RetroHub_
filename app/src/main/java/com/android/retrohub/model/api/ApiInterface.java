package com.android.retrohub.model.api;


import com.android.retrohub.model.data.GitToken;
import com.android.retrohub.model.data.UserInfo;
import com.android.retrohub.model.data.SearchReposList;
import com.android.retrohub.model.data.UserRepos;

import java.util.List;
import rx.Observable;


import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by batsa on 16.02.2017.
 */
public interface ApiInterface {
    /**
     *  GET annotation from our URL
     *
     */

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "User-Agent: Awesome-Octocat-App",
            "Accept: application/json"
    })
    @POST("login/oauth/access_token")
    Observable<GitToken> getAccessToken(
            @Query("code") String code,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("redirect_uri") String redirectUrl);

    @GET("user")
    Observable<UserInfo> getUserInfo(
            @Header("Authorization") String token
    );

    @GET("user/repos")
    Observable<List<UserRepos>> getUserRepos(@Header("Authorization") String token,
                                   @Query("sort") String sort,
                                   @Query("visibility") String visibility );

    @GET("search/repositories")
    Observable<SearchReposList> getSearchRepos(@Query("q") String q,
                                        @Query("sort") String sort,
                                        @Query("order") String order);
}
