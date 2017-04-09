package com.example.android.payup4;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class RecentsFragment extends Fragment{

    private String type_owe= "You owe ";
    private String type_owed = "You are owed";



    public RecentsFragment() {
        Log.v("Hii","In friends constructor");

        //required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.v("Hii", "In friends");

        View rootView = inflater.inflate(R.layout.activity_numbers1, container, false);

        Log.v("Hii","In friends");


        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Rucha","500",type_owe,"owner","Owner"));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_recent);
        Log.v("Hii","Words added");


        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        Log.v("Hii","friends fragment done");

        return rootView;


    }
}
