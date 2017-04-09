package com.example.android.payup4;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */


public class FriendsFragment extends Fragment{


    private String type_owe= "You owe";
    private String type_owed = "You are owed";
    private String type_pay= "You paid";
    private String type_paid = "You were paid";
    private String type_split = "Split equally";
    private String final_type;
    public final ArrayList<Word> words = new ArrayList<Word>();
    private int amt3=0;

    private TextView mEmptyStateTextView;// for empty listview
    private WordAdapter adapter;
    private  ListView listView;
    private ProgressBar mProgressBar;


    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;


    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;
    public DatabaseReference mMessagesDatabaseReference1;
    public ChildEventListener mChildEventListener;

    private String mUsername;

    public FirebaseAuth mFirebasAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;


    public FriendsFragment() {
        Log.v("Hii","In friends constructor");

        //required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("Hii","In friends");

        View rootView = inflater.inflate(R.layout.activity_numbers1, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("record");
        mFirebasAuth =  FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        adapter = new WordAdapter(getActivity(), words, R.color.category_friends);
        //Log.v("Hii","original Words added");

        listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);
        // Initialize progress bar
        //mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Word w1 = dataSnapshot.getValue(Word.class);//take my transaction and make a word object


                int i;
                int amtInt=0,amt2Int=0;

                //removing duplicates
                for(i=0;i<adapter.getCount();i++){
                    Word temp=adapter.getItem(i);
                    //Log.v("Hii","checking "+ temp.getName() +" with " + w1.getName());
                    if(temp.getName().equals(w1.getName()))
                    {
                            String amt = w1.getAmount(); // DB item(incoming)
                            String type_1 = w1.getowedType();
                            amtInt = Integer.parseInt(amt);
                            if (type_1.equals(type_owe) || type_1.equals(type_paid))
                            {
                                //if(type_1.equals(type_paid))
                                   //  Log.v("Hii", "making " + amtInt + "negative");
                                amtInt = amtInt * (-1);
                            }
                            else if (type_1.equals(type_split)) {
                                amtInt = 0;
                               // Log.v("Hii", "found db object with type split");
                            }

                            String amt2 = temp.getAmount();
                            amt2Int = Integer.parseInt(amt2);//adapter item
                            String type_2 = temp.getowedType();
                            if (type_2.equals(type_owe)|| type_2.equals(type_paid)) {
                                amt2Int = amt2Int * (-1);
                            }
                            else if (type_2.equals(type_split)) {
                               // Log.v("Hii","Found new object so resetting amount");
                                amt2Int = 0;
                            }

                            //Log.v("Hii","adding "+ amtInt +" with "+ amt2Int);
                            amt3 = amtInt + amt2Int;


                            if (amt3 > 0) {
                                adapter.getItem(i).setowedType(type_owed);
                                adapter.getItem(i).setAmount(Integer.toString(amt3));
                            } else {
                                adapter.getItem(i).setowedType(type_owe);
                                amt3 = amt3* (-1);
                                adapter.getItem(i).setAmount(Integer.toString(amt3));
                            }
                            //Log.v("Hii"," the incoming amount is "+ w1.getAmount());
                        //Log.v("Hii", " adapter amount for "+ temp.getName()+" is " + amt3 +
                          //      " and type is " + temp.getowedType());


                            break;
                        }

                }

                if(i==adapter.getCount()) {
                    adapter.add(w1);
                    //Log.v("Hii"," adapter amount of new word added is " + amt3 + " and amount is "+ w1.getAmount());
                    //Log.v("Hii", " person is " + w1.getName());


                }
               // Log.v("Hii","In listener"+w1.getName() + w1.getAmount());
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

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
        // mMessagesDatabaseReference1 = mFirebaseDatabase.getReference().child("settled");
        //mMessagesDatabaseReference1.addChildEventListener(mChildEventListener);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Word value = (Word)parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(),value.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(),FriendsFragmentDetail.class);
                intent.putExtra("name", value.getName());

                intent.putExtra("total",Integer.parseInt(adapter.getItem(position).getAmount()));
                final_type = adapter.getItem(position).getowedType();
                intent.putExtra("final_type",final_type);

                startActivity(intent);
            }
        });



        Log.v("Hii",listView.toString());

        Log.v("Hii","friends fragment done");

        return rootView;

    }

}
