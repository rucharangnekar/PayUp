package com.example.android.payup4;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class WordAdapter  extends ArrayAdapter<Word> {

    private int mColorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> words, int ColorResourceId) {
        super(context, 0, words);//calling ArrayAdapter constructor
        mColorResourceId = ColorResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);//dont use default list. take it from list_item.xml
        }


        Word currentWord = getItem(position);
        //Log.v("Hii","Adding data to list");

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.owed_name);
        nameTextView.setText(currentWord.getName());

        TextView amountTextView = (TextView) listItemView.findViewById(R.id.owed_amount);
        amountTextView.setText(currentWord.getAmount());

        TextView typeTextView = (TextView) listItemView.findViewById(R.id.owed_type);
        typeTextView.setText(currentWord.getowedType());

        View textContainer = listItemView.findViewById(R.id.complete);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        //Log.v("Hii","returning listItemView");
        return listItemView;
    }

}
