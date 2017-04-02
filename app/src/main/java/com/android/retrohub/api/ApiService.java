package com.android.retrohub.api;


import com.android.retrohub.models.GetToken;
import com.android.retrohub.models.GitUser;
import com.android.retrohub.models.SearcReposList;
import com.android.retrohub.models.UserRepos;

import java.util.List;

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


//    @GET("users/{user_login}/repos")
//    Call<List<UserRepos>> getRepos(@Path("user_login") String userLogin);

//    @Headers({
//            "sort: pushed",
//            "visibility: all"
//    })
    @GET("user/repos")
    Call<List<UserRepos>> getRepos(@Header("Authorization") String token,
                                   @Query("sort") String sort,
                                   @Query("visibility") String visibility );

    @GET("search/repositories")
    Call<SearcReposList> searchRepos(@Query("q") String q,
                                     @Query("sort") String sort,
                                     @Query("order") String order);



//    @GET("users//repos")
//    Call<UserReposList[]> getRoutes(@Path("user_login") String userLogin, Callback<UserReposList> routesCallback);
//
//    @DELETE("/applications/:{client_id}/grants/:{access_token}")
//    Call<Void> deleteBook(@Path("client_id") String bookId, @Path("access_token") String accessToken);


//    @Headers({
//            "Authorization: token f9d22f2abb9d6bcda2375a7b889bcf16aa36947e",
//    })
//    @GET("user")
//    Call<GitUser> getUser();







    /*
    @GET("users/andrey56097/repos")
    Call<UserRepos> getRepos();*/
}
