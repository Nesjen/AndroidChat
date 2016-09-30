package com.enesje.dev.nesjchat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Nesjen on 22.09.2016.
 */

public class FirebaseHandler {
    private String currentUsername;
    private FirebaseDatabase database;
    private String currentUserID;
    private boolean isDoneFindingUsername;

    public FirebaseHandler()
    {
         database = FirebaseDatabase.getInstance();
        isDoneFindingUsername = false;
         getUserIDFromDB();
         getCurrentUsernameFromDB();
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }




    private void getUserIDFromDB()
    {
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public boolean isDoneFindingUsername(){
        return this.isDoneFindingUsername;
    }

    public void setDoneFindingUsername(boolean isDoneFindingUsername)
    {
        this.isDoneFindingUsername = isDoneFindingUsername;
    }

    public String getCurrentUsernameFromDB()
    {
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String userID = postSnapshot.getKey(); //Gets the userID
                    System.out.println("Skal inn å sjekke om noke e likt!" + currentUserID);
                    if(userID.equals(currentUserID)){ //If userID match currentUserID
                        Contact tempContact = postSnapshot.getValue(Contact.class);
                        currentUsername = tempContact.getContactName(); //Gets username
                        isDoneFindingUsername = true;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                //ToDo fix noken feilmeldinga
            }
        });

        return currentUsername;
    }





}
