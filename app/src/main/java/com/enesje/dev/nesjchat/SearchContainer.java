package com.enesje.dev.nesjchat;

/**
 * Created by Eirik on 16.09.2016.
 */

public class SearchContainer {
    private int position;
    private Message msg;
    private String ConversationID;

    public SearchContainer(String conversationID, Message msg, int position) {
        ConversationID = conversationID;
        this.msg = msg;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public Message getMsg() {
        return msg;
    }

    public String getConversationID() {
        return ConversationID;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setConversationID(String conversationID) {
        ConversationID = conversationID;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }
}
