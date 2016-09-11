package com.enesje.dev.nesjchat;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Nesjen on 09.09.2016.
 */

public class SearchActivity extends AppCompatActivity {
    private CoordinatorLayout searchCoordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.searchResultCoordinator);


        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
           // String query = Intent.getStringExtra(SearchManager.QUERY);
           // doSearch(query);
        }
        Snackbar snackbar = Snackbar.make(searchCoordinatorLayout, "No settings available", Snackbar.LENGTH_LONG);
        snackbar.show();
    }



}
