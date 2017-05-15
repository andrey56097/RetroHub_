package com.android.retrohub.view;

import com.android.retrohub.model.data.SearchRepos;
import com.android.retrohub.model.data.SearchReposList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batsa on 11.05.2017.
 */

public interface SearchView extends BaseView {
    void showData(List<SearchRepos> searchReposes);
    void showEmptyList();
    String getSearchTerm();
}
