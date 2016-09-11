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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nesjen on 09.09.2016.
 */

public class SearchActivity extends AppCompatActivity {
    private CoordinatorLayout searchCoordinatorLayout;
    private List<String> contactNames;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        System.out.println("Mekker Search Activty!");
        contactNames = new ArrayList<>();
        lv = (ListView) findViewById(R.id.searchResultView);
        searchCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.searchResultCoordinator);
        contactNames.add("Eirik Nesje");
        contactNames.add("Inge Blaalid");


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, contactNames);
        lv.setAdapter(itemsAdapter);


        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
           // String query = Intent.getStringExtra(SearchManager.QUERY);
           // doSearch(query);
        }
        Snackbar snackbar = Snackbar.make(searchCoordinatorLayout, "No settings available", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
