package com.example.android.payup4;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class FFDAdapter  extends ArrayAdapter<Word> {

    private int mColorResourceId;

    public FFDAdapter(Activity context, ArrayList<Word> words, int ColorResourceId) {
        super(context, 0, words);//calling ArrayAdapter constructor
        mColorResourceId = ColorResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item1, parent, false);//dont use default list. take it from list_item.xml
        }


        Word currentWord = getItem(position);
        //Log.v("Hii","Adding data to list");

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.owed_name1);
        nameTextView.setText(currentWord.getName());

        TextView description = (TextView) listItemView.findViewById(R.id.desc);
            Log.v("Hii","Desc for amount "+ currentWord.getAmount() + " is " + currentWord.getDescription());
            description.setText(currentWord.getDescription());

        TextView amountTextView = (TextView) listItemView.findViewById(R.id.owed_amount1);
        amountTextView.setText(currentWord.getAmount());

        TextView typeTextView = (TextView) listItemView.findViewById(R.id.owed_type1);
        typeTextView.setText(currentWord.getowedType());

        View textContainer = listItemView.findViewById(R.id.complete1);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        //Log.v("Hii","returning listItemView");
        return listItemView;
    }

}
