package com.enesje.dev.nesjchat;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private ListView conversationView;
    private ArrayList<Conversation> conversations;
    private  ConversationAdapter adapter;
    private String currentUserID;
    private String myUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.maincoordinator);
        conversationView = (ListView) findViewById(R.id.conversationView);
        conversations = new ArrayList<>();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //myUsername = (String) getIntent().getSerializableExtra("myUsername");

        getUserNameFromDB();
        createDummyData();
        //showConversation();
        showConversationValue();
        handleNewMessageButton();


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

    public void showConversationMightBeDelted()
    {
                adapter = new ConversationAdapter(this, conversations);
                conversationView.setAdapter(adapter);
        conversationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Conversation conversation= (Conversation) parent.getAdapter().getItem(position);

                //System.out.println(conversation.getSender());
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("Conversation", conversation);
                startActivity(intent);
            }
        });

    }





    public void getUserNameFromDB()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String userID = postSnapshot.getKey();
                    if(userID.equals(currentUserID)){
                        Contact tempContact = postSnapshot.getValue(Contact.class);
                        myUsername = tempContact.getContactName();
                        System.out.println("***********MY USERNAME: " + myUsername);
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });

    }

    public void showConversation()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("conversation");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                conversations.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String conversationID = postSnapshot.getKey();

                    if(conversationID.contains(currentUserID)){
                            if(!haveLoadedConversation(conversationID)) {
                                for (DataSnapshot childPostSnapshot : postSnapshot.getChildren()) {

                                        Message newMsg = childPostSnapshot.getValue(Message.class);
                                       Conversation newConversation = new Conversation(conversationID, newMsg.getSenderOne(), "Siste melding");
                                        conversations.add(newConversation);
                                    break;


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
                                     newConversation = new Conversation(conversationID,conValues.getFriendlyNameTwo() , conValues.getLastMessage());
                                }
                                if(conValues.getUserIDTwo().equals(currentUserID))
                                {
                                     newConversation = new Conversation(conversationID,conValues.getFriendlyNameOne() , conValues.getLastMessage());
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


    public void getFriendlyName(String conversationID, String currentUserID)
    {



    }



    public void createDummyData()
    {
      //  Conversation one = new Conversation("Eirik Nesje");
      //  Conversation two = new Conversation("Birthe Vabø");
      //  Conversation three = new Conversation("Inge Blaalid");
      //  Conversation four = new Conversation("Kato Nesje");

      //  one.addMessage("Hallo");
      //  two.addMessage("Test Melding!");
      //  three.addMessage("Kan vi møtast i Stryn?");

      //  conversations.add(one);
      //  conversations.add(two);
      //  conversations.add(three);
    }

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

}
