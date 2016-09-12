package com.enesje.dev.nesjchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eirik on 12.09.2016.
 */

    public class ContactAdapter extends ArrayAdapter<Contact> {

        private ArrayList<Contact> contacts;

        public ContactAdapter(Context context, ArrayList<Contact> contacts) {
            super(context, 0, contacts);
            this.contacts = contacts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Contact currentContact = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contanct, parent, false);
            }
            TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
            // Populate the data into the template view using the data object
            contactName.setText(currentContact.getContactName());//
            // Return the completed view to render on screen
            return convertView;
        }

        public Contact getItem(int position){

            return contacts.get(position);
        }
    }