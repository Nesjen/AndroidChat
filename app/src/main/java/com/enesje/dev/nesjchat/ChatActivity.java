package com.enesje.dev.nesjchat;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eirik on 12.09.2016.
 */

public class ChatActivity extends AppCompatActivity {

    private CoordinatorLayout messageCoordinator;
    private ListView messageView;
    private EditText inputText;
    private Conversation conversation;
    private MessageAdapter messageAdapter;
    private Contact sender;
    private String currentUserID;
    private String senderID;
    private String messageToSend;
    private boolean gotConversation = false;
    private String conversationID = "";
    private String currentConversationID = "";
    String count = "1";
    private ArrayList<Message> messageList = new ArrayList<>();
    private String myUsername;
    private boolean shouldCreateConversation = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //myUsername = (String) getIntent().getSerializableExtra("myUsername");
        sender = (Contact) getIntent().getSerializableExtra("Contact");
        getSupportActionBar().setTitle(sender.getContactName());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        senderID = sender.getContactID();
        getUserNameFromDB();
        messageCoordinator = (CoordinatorLayout) findViewById(R.id.messageCoordinator);

        inputText = (EditText) findViewById(R.id.inputText);
        FloatingActionButton sendMessageButton = (FloatingActionButton) findViewById(R.id.sendMsgButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = inputText.getText().toString();
                //conversation.addMessage(message);
                messageToSend = message;
                String MessageID = "" + System.currentTimeMillis();
                Message msg = new Message(currentUserID,message,MessageID);
                addMessage();
                pushMsg(msg,MessageID);
                inputText.setText("");
            }
        });

        messageView = (ListView) findViewById(R.id.MessageView);
        showMessages();


    }

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



    public void showMessages()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("conversation");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    conversationID = postSnapshot.getKey();

                    if(conversationID.contains(currentUserID)){
                        if(conversationID.contains(senderID))
                        {
                            for (DataSnapshot childPostSnapshot: postSnapshot.getChildren()){
                                if(!haveLoadedMessage(childPostSnapshot.getKey()))
                                {
                                    Message newMsg = childPostSnapshot.getValue(Message.class);
                                    messageList.add(newMsg);
                                }

                            }

                        }
                    }
                }
                messageAdapter = new MessageAdapter(ChatActivity.this, messageList,currentUserID);
                messageView.setAdapter(messageAdapter);
                messageView.setSelection(messageAdapter.getCount() - 1);


            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });





    }


    public void showMessageFilterOutCurrentUser()
    {




    }


    public boolean haveLoadedMessage(String messageID)
    {
        boolean returnValue = false;
        for(int i = 0; i < messageList.size();i++)
        {
            if(messageList.get(i).getMessageID().equals(messageID)){
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void addMessage()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("conversation");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    conversationID = postSnapshot.getKey();

                    if(conversationID.contains(currentUserID)){
                        if(conversationID.contains(senderID))
                        {
                            currentConversationID = conversationID;
                            gotConversation = true;
                            count = postSnapshot.getChildrenCount() + "";
                        }
                    }
                }
                if(!gotConversation)
                {
                    shouldCreateConversation = true;
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });


    }


    public void pushMsg(Message message, String messageID)
    {
        System.out.println("Skal pushe!");
        //String lastMessage, String userIDTwo, String userIDOne, String getFriendlyNameTwo, String friendlyNameOne
        ConversationValues conVal = new ConversationValues(message.getMessage(),currentUserID,senderID,myUsername,sender.getContactName());
        System.out.println("******************Myusername:" + sender.getContactName());
        System.out.println("Sender:" + myUsername);

        if(gotConversation){
            System.out.println("hadde conv!");
            if(currentConversationID.equals(currentUserID + "|" + senderID)) {
                System.out.println("Var lik 1");
                FirebaseDatabase.getInstance().getReference("conversation").child(currentUserID + "|" + senderID).child(messageID).setValue(message);
                FirebaseDatabase.getInstance().getReference("conversationValues").child(currentUserID + "|" + senderID ).setValue(conVal);
            }

            if(currentConversationID.equals(senderID + "|" + currentUserID)) {
                System.out.println("Va lik 2!");
                FirebaseDatabase.getInstance().getReference("conversation").child(senderID + "|" + currentUserID).child(messageID).setValue(message);
                FirebaseDatabase.getInstance().getReference("conversationValues").child(senderID + "|" + currentUserID).setValue(conVal);


            }
        }

        if(shouldCreateConversation){
            System.out.println("Hadde ikke conc!!");
            currentConversationID = currentUserID + "|" + senderID;
            gotConversation = true;
            shouldCreateConversation = false;
            FirebaseDatabase.getInstance().getReference("conversation").child(currentUserID + "|" + senderID).child(messageID).setValue(message);
            FirebaseDatabase.getInstance().getReference("conversationValues").child(currentUserID + "|" + senderID ).setValue(conVal);

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
            Snackbar snackbar = Snackbar.make(messageCoordinator, "No settings available", Snackbar.LENGTH_LONG);
            snackbar.show();
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
}
