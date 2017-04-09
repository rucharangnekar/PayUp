package com.example.android.payup4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class ExpenseFragment extends Fragment {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;


    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;
    public DatabaseReference mMessagesDatabaseReference1;
    public ChildEventListener mChildEventListener;

    private String mUsername;

    public FirebaseAuth mFirebasAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;

    private ExpenseAdapter adapter11;


    public ExpenseFragment() {
        Log.v("Hii","In expenses constructor");

        //required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("Hii","In expenses");

        View rootView = inflater.inflate(R.layout.activity_numbers1, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("expense");
        mFirebasAuth =  FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);


        Log.v("Hii","In expense frag");

        final ArrayList<Personal> expenses = new ArrayList<Personal>();

        //expenses.add(new Personal("Food","Roo","Cash","5000","Sent"));

        adapter11 = new ExpenseAdapter(getActivity(), expenses,  R.color.category_friends);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter11);

        Log.v("Hii","expense fragment done");



        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.v("Hii","Searching db");
                Personal expenses = dataSnapshot.getValue(Personal.class);

                Log.v("Hii","Sent is " + expenses.getReason() + expenses.getSent());
                adapter11.add(expenses);
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

        Log.v("Hii","all expenses done");


        return rootView;
    }
}
