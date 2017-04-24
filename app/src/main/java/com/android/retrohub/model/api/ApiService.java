package com.android.retrohub.model.api;


import com.android.retrohub.model.GetToken;
import com.android.retrohub.model.GitUser;
import com.android.retrohub.model.SearchReposList;
import com.android.retrohub.model.UserRepos;

import java.util.List;
import rx.Observable;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by batsa on 16.02.2017.
 */
public interface ApiService {
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
    Call<GetToken> getMyJSON(
            @Query("code") String code,
            @Query("client_id") String clienId,
            @Query("client_secret") String clientSecret,
            @Query("redirect_uri") String redirectUrl,
            @Query("grant_type") String grandType);

    @GET("user")
    Call<GitUser> getUser(
            @Header("Authorization") String token
    );

    @GET("user/repos")
    Call<List<UserRepos>> getRepos(@Header("Authorization") String token,
                                   @Query("sort") String sort,
                                   @Query("visibility") String visibility );

    @GET("search/repositories")
    Call<SearchReposList> searchRepos(@Query("q") String q,
                                      @Query("sort") String sort,
                                      @Query("order") String order);
}