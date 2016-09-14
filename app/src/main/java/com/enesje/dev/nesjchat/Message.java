package com.enesje.dev.nesjchat;

import java.io.Serializable;

/**
 * Created by Eirik on 14.09.2016.
 */

public class Message implements Serializable{
    private String senderOne;
    private String message;

    public Message(String senderOne, String message) {
        this.message = message;
        this.senderOne = senderOne;
    }

    public Message()
    {

    }

    public void setSenderOne(String senderOne) {
        this.senderOne = senderOne;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getSenderOne() {
        return senderOne;
    }

    public String getMessage() {
        return message;
    }
}
