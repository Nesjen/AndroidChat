package com.enesje.dev.nesjchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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
        SearchContainer searchContainer = getItem(position);
        searchContainer.setPosition(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_searchresult, parent, false);
        }
        TextView senderTV = (TextView) convertView.findViewById(R.id.search_message_sender);
        TextView messageTV = (TextView) convertView.findViewById(R.id.search_message_message);

        if(!searchContainer.getMsg().getSenderOne().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            senderTV.setText(searchContainer.getMsg().getSenderOne());//
            messageTV.setText(searchContainer.getMsg().getMessage());//

        }

        return convertView;
    }

    public SearchContainer getItem(int position){

        return searchContainer.get(position);
    }
}
