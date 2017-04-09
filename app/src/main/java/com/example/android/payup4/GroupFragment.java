package com.example.android.payup4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class GroupFragment extends Fragment {

    private String type_owe= "You owe ";
    private String type_owed = "You are owed";


    public GroupFragment() {
        Log.v("Hii","In group constructor");

        //required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("Hii","In friends");

        View rootView = inflater.inflate(R.layout.activity_numbers1, container, false);


        Log.v("Hii","In friends");

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Rucha","500",type_owe,"owner","Owner"));

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_group);
        Log.v("Hii","Words added");


        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        Log.v("Hii","friends fragment done");

        return rootView;
    }
}
