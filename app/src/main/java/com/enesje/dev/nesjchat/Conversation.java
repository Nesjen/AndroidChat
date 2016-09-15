package com.enesje.dev.nesjchat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eirik on 11.09.2016.
 */

public class Conversation implements Serializable {
    private String sender;
    private String lastMessage;
    private String conversationID;

    public Conversation(String conversationID, String sender, String lastMessage) {
        this.conversationID = conversationID;
        this.lastMessage = lastMessage;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getConversationID() {
        return conversationID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
