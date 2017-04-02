package com.android.retrohub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by batsa on 29.03.2017.
 */

public class SearchReposList {

    @SerializedName("items")
    @Expose
    private ArrayList<SearchRepos> searchReposes = new ArrayList<>();

    public ArrayList<SearchRepos> getContacts() {
        return searchReposes;
    }

    public void setContacts(ArrayList<SearchRepos> contacts) {
        this.searchReposes = contacts;
    }
}
