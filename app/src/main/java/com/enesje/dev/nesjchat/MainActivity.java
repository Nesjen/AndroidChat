package com.enesje.dev.nesjchat;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private CoordinatorLayout coordinatorLayout;
    private ListView conversationView;
    private ArrayList<Conversation> conversations;
    private ConversationAdapter adapter;
    private String currentUserID;
    private String myUsername;
    private  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.maincoordinator);
        conversationView = (ListView) findViewById(R.id.conversationView);
        conversations = new ArrayList<>();


        //For Firebase handling:
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Gets current userID (currentUserID)
        getUserNameFromDB(); // Gets current username (myUsername)





        //Display conversation on mainscreen
        showConversationValue();

        //Handle FAB - new message button.
        handleNewMessageButton();

        //Handle clicks on conversations
        handleConversationClicks();

        //Navbar
        navBar();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.main_search);

        SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
        SearchManager sMngr = (SearchManager)getSystemService(SEARCH_SERVICE);
        SearchableInfo searchableInfo = sMngr.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.logout)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No settings available", Snackbar.LENGTH_LONG);
            snackbar.show();
            return true;
        }

        if (id == R.id.main_search) {
            return true;
        }

        if (id == R.id.action_about) {
            System.out.println("VI HAR SO MONGE: " + conversations.size());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Single value FB-eventlistner to init username.
     */
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

    /**
     *  Gets all ConversationValues(Sender/reciever + lastmessage) and displays them on screen with conversationAdapter.
     */
    public void showConversationValue()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("conversationValues");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                conversations.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String conversationID = postSnapshot.getKey();

                    if(conversationID.contains(currentUserID)){
                        if(!haveLoadedConversation(conversationID)) {

                                Conversation newConversation = null;
                                ConversationValues conValues = postSnapshot.getValue(ConversationValues.class);

                                if(conValues.getUserIDOne().equals(currentUserID))
                                {
                                     newConversation = new Conversation(conversationID,conValues.getFriendlyNameTwo() , conValues.getLastMessage(),conValues.getUserIDTwo());
                                }
                                if(conValues.getUserIDTwo().equals(currentUserID))
                                {
                                     newConversation = new Conversation(conversationID,conValues.getFriendlyNameOne() , conValues.getLastMessage(),conValues.getUserIDOne());
                                }
                                if(newConversation != null){
                                    conversations.add(newConversation);

                                }

                        }
                    }
                }
                adapter = new ConversationAdapter(MainActivity.this, conversations);
                conversationView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });





    }

    /**
     * Checks if conversation is already loaded
     * @param conversationID - the conversation ID you want to see if already exsist.
     * @return boolean - True if conversation exsist.
     */
    public boolean haveLoadedConversation(String conversationID)
    {
        boolean returnValue = false;
        for(int i = 0; i < conversations.size();i++)
        {
            if(conversations.get(i).getConversationID().equals(conversationID)){
                returnValue = true;
            }
        }
        return returnValue;

    }


    /**
     *  Handles FAB for new Messages.
     */
    public void handleNewMessageButton()
    {
        FloatingActionButton newChatButton = (FloatingActionButton) findViewById(R.id.newChatButton);
        newChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("Conversations", conversations);
                intent.putExtra("myUsername", myUsername);
                startActivity(intent);
            }
        });
    }

    public void handleConversationClicks()
    {
        conversationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Conversation conversation= (Conversation) parent.getAdapter().getItem(position);
                Contact contact = new Contact(conversation.getSenderID(),conversation.getSender()); //String contactID, String contactName
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("Contact", contact);
                intent.putExtra("myUsername", myUsername);
                startActivity(intent);
            }
        });

    }




    //TODO - save conversations locally
    public void saveConversations()
    {

    }


    public void navBar()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

}
