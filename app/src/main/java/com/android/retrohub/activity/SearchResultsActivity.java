package com.android.retrohub.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.retrohub.R;
import com.android.retrohub.adapter.SearchReposAdapter;
import com.android.retrohub.model.SearchReposList;
import com.android.retrohub.model.SearchRepos;
import com.android.retrohub.model.api.ApiService;
import com.android.retrohub.model.api.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by batsa on 15.03.2017.
 */
public class SearchResultsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TextView textView;
    private SearchReposAdapter adapter;
    private ListView listView;

    private ArrayList<SearchRepos> searchReposes;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String query;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        searchReposes = new ArrayList<>();
        handleIntent(getIntent());


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh2);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(this);

        listView = (ListView) findViewById(R.id.listView2);
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
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

            final ApiService api = RetroClient.getApiServiceGIT();


//            Call<List<UserRepos>> call = api.getRepos(MainActivity.token, "pushed", "all");
            Call<SearchReposList> call = api.searchRepos(query,"stars","desc");

            final ProgressDialog dialog;
            dialog = new ProgressDialog(SearchResultsActivity.this);
            dialog.setTitle(getString(R.string.string_getting_gson_title));
            dialog.setMessage(getString(R.string.string_getting_gson_massage));
            dialog.show();

            call.enqueue(new Callback<SearchReposList>() {
                @Override
                public void onResponse(Call<SearchReposList> call, Response<SearchReposList> response) {
                    dialog.dismiss();

                    try {
//                        dataArrayList = response.body().getItems().get(3).;

                        searchReposes = (ArrayList<SearchRepos>) response.body().getItems();
                        adapter = new SearchReposAdapter(SearchResultsActivity.this, searchReposes);
                        listView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);


//                        Log.e("SIZE", dataArrayList.size() + "");
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<SearchReposList> call, Throwable t) {
                    dialog.dismiss();
                }
            });

//            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchResultsActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchResultsActivity.this.getComponentName()));
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

        final ApiService api = RetroClient.getApiServiceGIT();

        Call<SearchReposList> call = api.searchRepos(query,"stars","desc");

        final ProgressDialog dialog;
        dialog = new ProgressDialog(SearchResultsActivity.this);
        dialog.setTitle(getString(R.string.string_getting_gson_title));
        dialog.setMessage(getString(R.string.string_getting_gson_massage));
        dialog.show();

        call.enqueue(new Callback<SearchReposList>() {
            @Override
            public void onResponse(Call<SearchReposList> call, Response<SearchReposList> response) {
                dialog.dismiss();

                try {
                    searchReposes = (ArrayList<SearchRepos>) response.body().getItems();
                    adapter = new SearchReposAdapter(SearchResultsActivity.this, searchReposes);
                    listView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SearchReposList> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}