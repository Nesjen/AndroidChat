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

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
        }
        // Lookup view for data population
        TextView senderName = (TextView) convertView.findViewById(R.id.senderName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);
        // Populate the data into the template view using the data object
        senderName.setText(message.getSenderOne());
        lastMessage.setText(message.getMessage());
        // Return the completed view to render on screen
        return convertView;
    }

    public Message getItem(int position){

        return messages.get(position);
    }
}