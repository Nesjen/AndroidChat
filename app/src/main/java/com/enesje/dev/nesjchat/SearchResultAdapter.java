package com.enesje.dev.nesjchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eirik on 16.09.2016.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchContainer> {

    private ArrayList<SearchContainer> searchContainer;

    public SearchResultAdapter(Context context, ArrayList<SearchContainer> searchContainer) {
        super(context, 0, searchContainer);
        this.searchContainer = searchContainer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SearchContainer searchContainer = getItem(position);
        searchContainer.setPosition(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contanct, parent, false);
        }
        TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
        // Populate the data into the template view using the data object
        contactName.setText(searchContainer.getMsg().getMessage());//
        // Return the completed view to render on screen
        return convertView;
    }

    public SearchContainer getItem(int position){

        return searchContainer.get(position);
    }
}
