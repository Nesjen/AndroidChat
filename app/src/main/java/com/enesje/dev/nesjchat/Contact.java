package com.enesje.dev.nesjchat;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Eirik on 12.09.2016.
 */

public class Contact implements Serializable {
    private String contactName;
    private String contactID;

    public Contact(String contactID, String contactName) {
        this.contactName = contactName;
        this.contactID = contactID;
    }

    public Contact() {

    }

    public String getContactName() {
        return contactName;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }
}

