package com.enesje.dev.nesjchat;

import java.io.Serializable;

/**
 * Created by Eirik on 14.09.2016.
 */

public class Message implements Serializable{
    private String senderOne;
    private String message;
    private String messageID;

    public Message(String senderOne, String message, String messageID) {
        this.message = message;
        this.senderOne = senderOne;
        this.messageID = messageID;
    }

    public Message()
    {

    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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
