package com.enesje.dev.nesjchat;

import android.*;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Eirik on 12.09.2016.
 */
public class ContactActivity extends AppCompatActivity {

    private CoordinatorLayout contactCoordinator;
    private ListView contactView;
    private ArrayList<Contact> contacts;
    private ArrayList<Conversation>  conversations;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ContactAdapter adapter;
    private String myUsername;
 //   private ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Select Contact");
        myUsername = (String) getIntent().getSerializableExtra("myUsername");

        conversations = (ArrayList<Conversation>) getIntent().getSerializableExtra("Conversations");

        contactCoordinator = (CoordinatorLayout) findViewById(R.id.contactCoordinator);
        contactView = (ListView) findViewById(R.id.contactView);
        contacts = new ArrayList<>();
        showContacts();
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
            Snackbar snackbar = Snackbar.make(contactCoordinator, "No settings available", Snackbar.LENGTH_LONG);
            snackbar.show();
            return true;
        }

        if(id == R.id.action_about)
        {
            System.out.println("VI HAR SO MONGE: " + conversations.size());

            return true;
        }


        if (id == R.id.main_search) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            getContacts();
        }
    }


    private void getContacts()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                  Contact post = postSnapshot.getValue(Contact.class);
                  System.out.println("SJÅ HER: " + post.getContactName());
                    contacts.add(post);

                }
                adapter = new ContactAdapter(ContactActivity.this, contacts);
                contactView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


       // adapter = new ContactAdapter(this, contacts);
       // contactView.setAdapter(adapter);
        contactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Contact contact= (Contact) parent.getAdapter().getItem(position);

            Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
            intent.putExtra("Contact", contact);
            intent.putExtra("myUsername", myUsername);
            startActivity(intent);
        }
    });


    }


}

