package com.enesje.dev.nesjchat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eirik on 11.09.2016.
 */

public class Conversation implements Serializable {
    private String sender;
    private String lastMessage;
    private ArrayList<String> messages;

    public Conversation(String sender)
    {
        this.sender = sender;
        lastMessage = "";
        messages = new ArrayList<>();
    }

    public String getSender() {
        return sender;
    }

    public String getLastMessage() {
        if(messages.get(messages.size()-1) != null)
        {
            return messages.get(messages.size()-1);
        }else{
            return "";
        }
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void addMessage(String newMessage) {
         messages.add(newMessage);
    }

    public boolean haveConversation()
    {
        if(messages.get(messages.size()-1) != null){
            return true;
        }else
        {
            return false;
        }
    }
}
