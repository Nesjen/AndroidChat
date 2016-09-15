package com.enesje.dev.nesjchat;

import java.io.Serializable;

/**
 * Created by Nesjen on 15.09.2016.
 */

public class ConversationValues implements Serializable {
    private String lastMessage;
    private String friendlyNameOne;
    private String friendlyNameTwo;
    private String UserIDOne;
    private String userIDTwo;

    public ConversationValues(String lastMessage, String userIDTwo, String userIDOne, String friendlyNameTwo, String friendlyNameOne) {
        this.lastMessage = lastMessage;
        this.userIDTwo = userIDTwo;
        UserIDOne = userIDOne;
        this.friendlyNameTwo = friendlyNameTwo;
        this.friendlyNameOne = friendlyNameOne;
    }

    public ConversationValues()
    {

    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getUserIDTwo() {
        return userIDTwo;
    }

    public String getUserIDOne() {
        return UserIDOne;
    }

    public String getFriendlyNameTwo() {
        return friendlyNameTwo;
    }

    public String getFriendlyNameOne() {
        return friendlyNameOne;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUserIDTwo(String userIDTwo) {
        this.userIDTwo = userIDTwo;
    }

    public void setUserIDOne(String userIDOne) {
        UserIDOne = userIDOne;
    }

    public void setFriendlyNameOne(String friendlyNameOne) {
        this.friendlyNameOne = friendlyNameOne;
    }

    public void setFriendlyNameTwo(String friendlyNameTwo) {
        this.friendlyNameTwo = friendlyNameTwo;
    }
}
