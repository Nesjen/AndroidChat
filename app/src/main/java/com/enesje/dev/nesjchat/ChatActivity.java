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
    private ListView messages;
    private EditText inputText;
    private Conversation conversation;
    private ConversationAdapter conversationAdapter;
    private Contact sender;
    private String currentUserID;
    private String senderID;
    private String messageToSend;
    private boolean gotConversation = false;
    private String conversationID = "";
    private String currentConversationID = "";
    String count = "1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sender = (Contact) getIntent().getSerializableExtra("Contact");
        getSupportActionBar().setTitle(sender.getContactName());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        senderID = sender.getContactID();

       // MessageConversation messageConversation = new MessageConversation("test");
       // Message message = new Message(currentUserID, senderID,"Hallo!");
        FirebaseDatabase.getInstance().getReference("conversation").child(currentUserID + "|" + "2323123").child("1").setValue("Mendling1");


        messageCoordinator = (CoordinatorLayout) findViewById(R.id.messageCoordinator);

        inputText = (EditText) findViewById(R.id.inputText);
        FloatingActionButton sendMessageButton = (FloatingActionButton) findViewById(R.id.sendMsgButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = inputText.getText().toString();
                //conversation.addMessage(message);
                messageToSend = message;

                Message msg = new Message(currentUserID,message);
                addMessage();
                pushMsg(msg);
                  //  conversationAdapter.notifyDataSetChanged();
                inputText.setText("");
            }
        });

        showMessages();


    }

    public void showMessages()
    {

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


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


    }


    public void pushMsg(Message message)
    {
        System.out.println("Skal pushe!");
        if(gotConversation){
            System.out.println("hadde conv!");
            if(currentConversationID.equals(currentUserID + "|" + senderID)) {
                System.out.println("Var lik 1");
                FirebaseDatabase.getInstance().getReference("conversation").child(currentUserID + "|" + senderID).child(""+ System.currentTimeMillis()).setValue(message);
            }

            if(currentConversationID.equals(senderID + "|" + currentUserID)) {
                System.out.println("Va lik 2!");
                FirebaseDatabase.getInstance().getReference("conversation").child(senderID + "|" + currentUserID).child(""+System.currentTimeMillis()).setValue(message);

            }
        }else{
            System.out.println("Hadde ikke conc!!");
            currentConversationID = currentUserID + "|" + senderID;
            gotConversation = true;
            FirebaseDatabase.getInstance().getReference("conversation").child(currentUserID + "|" + senderID).child(""+System.currentTimeMillis()).setValue(message);
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
