package com.enesje.dev.nesjchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eirik on 11.09.2016.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    private String currentUserID;
    public MessageAdapter(Context context, ArrayList<Message> messages, String currentUserID) {
        super(context, 0, messages);
        this.messages = messages;
        this.currentUserID = currentUserID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if (message.getSenderOne().equals(currentUserID)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_send, parent, false);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_rec, parent, false);
        }
        TextView textMessage = (TextView) convertView.findViewById(R.id.messageText);
        textMessage.setText(message.getMessage());
        return convertView;
    }

    public Message getItem(int position){

        return messages.get(position);
    }
}