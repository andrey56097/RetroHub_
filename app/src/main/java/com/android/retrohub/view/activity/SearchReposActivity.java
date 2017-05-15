package com.android.retrohub.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.retrohub.R;
import com.android.retrohub.model.data.SearchRepos;
import com.android.retrohub.model.data.SearchReposList;
import com.android.retrohub.presenter.MainPresenter;
import com.android.retrohub.presenter.SearchPresenter;
import com.android.retrohub.view.adapter.SearchReposAdapter;
import com.android.retrohub.view.adapter.UserReposAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.retrohub.utils.AndroidApp.hasNetwork;

/**
 * Created by batsa on 11.05.2017.
 */

public class SearchReposActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, com.android.retrohub.view.SearchView {

    @BindView(R.id.listViewSearch) ListView listView;
    @BindView(R.id.loadingViewSearch) ProgressBar loadingView;
    @BindView(R.id.swipe_to_refresh_search) SwipeRefreshLayout swipeRefreshLayout;

    private String query;

    private SearchReposAdapter adapter;
    private SearchPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        presenter = new SearchPresenter(this, this);
        handleIntent(getIntent());

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            presenter.getData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchReposActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQuery(query, false);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        listView.setAdapter(null);
        if (!hasNetwork()) {
            makeSnack(getResources().getString(R.string.string_internet_connection_error));
        }
        presenter.getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void makeSnack(String text) {
        Snackbar snackbar = Snackbar.make(loadingView, text, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public void showError(String error) {
        makeSnack(error);
    }

    @Override
    public void showProgressView() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressView() {
        loadingView.setVisibility(View.GONE);
    }


    @Override
    public void showData(List<SearchRepos> searchReposes) {
        adapter = new SearchReposAdapter(SearchReposActivity.this, searchReposes);
        listView.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        listView.setAdapter(null);
        makeSnack(getResources().getString(R.string.empty_list));
    }

    @Override
    public String getSearchTerm() {
        return query;
    }
}
