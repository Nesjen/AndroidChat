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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nesjen on 09.09.2016.
 */

public class SearchActivity extends AppCompatActivity {

    private CoordinatorLayout searchCoordinatorLayout;
    private ArrayList<SearchContainer> searchResults;
    private ListView searchResultView;
    private String query = "";
    private String currentUserID;
    private String myUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchResultView = (ListView) findViewById(R.id.searchResultView);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
       // getUserNameFromDB();

        searchResults = new ArrayList<>();
        Intent searchIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction()))
        {
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            this.query = query;
            searchConversations(query);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.main_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager sMngr = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = sMngr.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Snackbar snackbar = Snackbar.make(searchCoordinatorLayout, "No settings available", Snackbar.LENGTH_LONG);
            snackbar.show();
            return true;
        }

        if (id == R.id.main_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Søker kun gjennom mottatte meldinger!
     * @param query
     */
    public void searchConversations(final String query)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("conversation");
        searchResults.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String conversationID = postSnapshot.getKey();
                    if(conversationID.contains(currentUserID)) {
                        for (DataSnapshot childSnap : postSnapshot.getChildren()) {
                            Message newMsg = childSnap.getValue(Message.class);
                            if (newMsg.getMessage().contains(query)) {
                                conversationID = postSnapshot.getKey();

                                SearchContainer searchRow = new SearchContainer(conversationID, newMsg, 22);
                                if(!searchRow.getMsg().getSenderOne().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    searchResults.add(searchRow); //Add Result
                                }

                            }
                        }
                    }
                }
                showResults();



            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });
    }


    public void showResults()
    {
        SearchResultAdapter messageAdapter = new SearchResultAdapter(SearchActivity.this, searchResults);
        searchResultView.setAdapter(messageAdapter);
        searchResultView.setSelection(messageAdapter.getCount() - 1);

       // searchResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          //  @Override
          //  public void onItemClick(AdapterView<?> parent, View view, int position,
          //                          long id) {
              //  SearchContainer searchContainer= (SearchContainer) parent.getAdapter().getItem(position);
              //  Contact contact = new Contact(searchContainer.getMsg().getSenderOne(),conversation.getSender()); //String contactID, String contactName
              //  Intent intent = new Intent(SearchActivity.this, ChatActivity.class);
              //  intent.putExtra("Contact", contact);
              //  intent.putExtra("myUsername", myUsername);
              //  startActivity(intent);
        //    }
       // });
    }

        //Todo, put in seperate class
        public void getUserNameFromDB()
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        String userID = postSnapshot.getKey(); //Gets the userID
                        if(userID.equals(currentUserID)){ //If userID match currentUserID
                            Contact tempContact = postSnapshot.getValue(Contact.class);
                            myUsername = tempContact.getContactName(); //Gets username
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    //ToDo fix noken feilmeldinga
                }
            });

        }






}
