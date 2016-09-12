package com.enesje.dev.nesjchat;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Eirik on 12.09.2016.
 */

public class Contact implements Serializable {
    private String contactName;
    private Uri contactImage;
    private String contactNr;
    private Long contactID;

    public Contact(Uri contactImage, String contactName, Long contactID, String contactNr) {
        this.contactImage = contactImage;
        this.contactName = contactName;
        this.contactID = contactID;
        this.contactNr = contactNr;
    }

    public String getContactName() {
        return contactName;
    }

    public Uri getContactImage() {
        return contactImage;
    }

    public String getContactNr() {
        return contactNr;
    }

    public Long getContactID() {
        return contactID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactImage(Uri contactImage) {
        this.contactImage = contactImage;
    }

    public void setContactNr(String contactNr) {
        this.contactNr = contactNr;
    }

    public void setContactID(Long contactID) {
        this.contactID = contactID;
    }
}

