package com.enesje.dev.nesjchat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eirik on 14.09.2016.
 */

public class MessageConversation implements Serializable {
  //  private List<Message> conversation = new ArrayList<>();
    private String test;

    public MessageConversation(String test) {
        this.test = test;
    }
    public MessageConversation()
    {

    }

    public void addMessage(Message msg)
    {
    //    conversation.add(msg);
    }

   // public List<Message> getConversation()
   // {
      //  return conversation;
   // }

}
