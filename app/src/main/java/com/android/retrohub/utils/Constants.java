package com.android.retrohub.utils;

/**
 * Created by batsa on 22.04.2017.
 */

public final class Constants {

    public static final String ROOT_URL = "https://github.com/";
    public static final String ROOT_URL_GIT = "https://api.github.com/";

    public static final String CLIENT_ID = "500acef59d850d1cf45c";
    public static final String CLIENT_SECRET = "7b5dcf5e074b4c6c75016c64ffae9d7e491b0380";
    public static final String REDIRECT_URL = "http://localhost";
    public static final String API_BASE_URL = "https://github.com/login/oauth/authorize";
    public static final String SCOPE = "&scope=repo_hook%20repo%20public_repo%20admin:public_key";

    /**
     *  Parameters for user repos
     */
    public static final String SORT_REPO = "pushed";
    public static final String VISABILITY = "all";

    /**
     *  Parameters for search repos
     */
    public static final String SORT = "stars";
    public static final String ORDER = "desc";
}
