package com.example.android.payup4;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 23-03-2017.
 */

public class ExpenseAdapter extends ArrayAdapter<Personal>{

    private int mColorResourceId;

    public ExpenseAdapter(Activity context, ArrayList<Personal> expenses, int ColorResourceId) {
        super(context, 0, expenses);//calling ArrayAdapter constructor
        mColorResourceId = ColorResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.personal_list_item, parent, false);//dont use default list. take it from list_item.xml
        }


        Personal currentExpense = getItem(position);
        //Log.v("Hii","Adding data to list");

        TextView reason = (TextView) listItemView.findViewById(R.id.reason);
        reason.setText(currentExpense.getReason());

        TextView amount = (TextView) listItemView.findViewById(R.id.amount);
        amount.setText(currentExpense.getAmount());

        TextView account = (TextView) listItemView.findViewById(R.id.account);
        account.setText(currentExpense.getAccount());


        Log.v("Hii", "Sent state is "+ currentExpense.getSent());


        if(currentExpense.getSent().charAt(0) == 'R')
        {
            int redd = R.color.green;
            Log.v("Hii", "In green");
            int color = ContextCompat.getColor(getContext(), redd);
            amount.setTextColor(color);
        }
        else
        {
            int redd = R.color.red;
            Log.v("Hii", "In red");
            int color = ContextCompat.getColor(getContext(), redd);
            amount.setTextColor(color);
        }

        View textContainer = listItemView.findViewById(R.id.complete1);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        Log.v("Hii","returning listItemView in expenses");
        return listItemView;
    }


}
