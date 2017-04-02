package com.android.retrohub.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.retrohub.R;
import com.android.retrohub.adapter.UserReposAdapter;
import com.android.retrohub.api.ApiService;
import com.android.retrohub.api.RetroClient;
import com.android.retrohub.models.UserRepos;
import com.android.retrohub.services.InternetConnection;
import com.squareup.picasso.Picasso;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, DrawerLayout.DrawerListener {


    public static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    List<UserRepos> dataArrayList;

    private UserReposAdapter adapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        boolean  firstTime=sharedPreferences.getBoolean("first", true);


            if (firstTime) {
                editor.putBoolean("first", false);
                editor.commit();

                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }


        setContentView(R.layout.activity_main);

        invalidateOptionsMenu();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.userLogin);
        ImageView img = (ImageView)header.findViewById(R.id.imageView);


        listView = (ListView) findViewById(R.id.listView);

        name.setText(sharedPreferences.getString("login","your login"));

        Picasso.with(getApplicationContext())
                .load(sharedPreferences.getString("imageUrl","xxx"))
                .error(R.mipmap.ic_launcher).into(img);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(this);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(this);
        toggle.syncState();


        /**
         * Getting user Repos
         */


            final ApiService api = RetroClient.getApiServiceGIT();
            Call<List<UserRepos>> call = api.getRepos(sharedPreferences.getString("token",""), "pushed", "all");

            final ProgressDialog dialog;
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getString(R.string.string_getting_gson_title));
            dialog.setMessage(getString(R.string.string_getting_gson_massage));
            dialog.show();

            call.enqueue(new Callback<List<UserRepos>>() {
                @Override
                public void onResponse(Call<List<UserRepos>> call, Response<List<UserRepos>> response) {
                    dialog.dismiss();

                    try {
                        dataArrayList = response.body();

                        adapter = new UserReposAdapter(MainActivity.this, dataArrayList);
                        listView.setAdapter(adapter);

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<List<UserRepos>> call, Throwable t) {

                    dialog.dismiss();

                    Log.e("ERROR WP", " " + t.toString());
                }
            });

    }


    @Override
    public void onBackPressed() {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            CookieManager cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(null);
            } else {
                cookieManager.removeAllCookie();
            }

            editor.putBoolean("first", false);

            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onRefresh() {
        final ApiService api = RetroClient.getApiServiceGIT();
        Call<List<UserRepos>> call = api.getRepos( sharedPreferences.getString("token",""),"pushed","all");

        final ProgressDialog dialog;
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle(getString(R.string.string_getting_gson_title));
        dialog.setMessage(getString(R.string.string_getting_gson_massage));
        dialog.show();

        call.enqueue(new Callback<List<UserRepos>>() {
            @Override
            public void onResponse(Call<List<UserRepos>> call, Response<List<UserRepos>> response) {
                dialog.dismiss();

                try {
                    dataArrayList = response.body();

                    adapter = new UserReposAdapter(MainActivity.this, dataArrayList);
                    listView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e){}
            }

            @Override
            public void onFailure(Call<List<UserRepos>> call, Throwable t) {

                dialog.dismiss();
                Log.e("ERROR WP", " "+t.toString());
            }
        });
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
