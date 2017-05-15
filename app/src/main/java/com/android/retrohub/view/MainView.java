package com.android.retrohub.view;

import com.android.retrohub.model.data.UserRepos;

import java.util.List;

/**
 * Created by batsa on 10.05.2017.
 */

public interface MainView extends BaseView {
    void showData(List<UserRepos>  userReposes);
    void showEmptyList();
}
