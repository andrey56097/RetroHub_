package com.android.retrohub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by batsa on 29.03.2017.
 */

public class SearcReposList {
    @SerializedName("total_count")
    @Expose
    private int totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<SearchRepos> items = null;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<SearchRepos> getItems() {
        return items;
    }

    public void setItems(List<SearchRepos> items) {
        this.items = items;
    }
}
