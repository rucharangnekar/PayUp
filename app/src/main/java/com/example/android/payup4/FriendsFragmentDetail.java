package com.example.android.payup4;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragmentDetail extends AppCompatActivity {

    private String person;
    public FirebaseDatabase mFirebaseDatabase;

    public FirebaseDatabase mFirebaseDatabase1;
    public DatabaseReference mMessagesDatabaseReference;
    public ChildEventListener mChildEventListener;

    private String mUsername;
    private ProgressBar mProgressBar1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_fragment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("record");


        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        Intent intent = getIntent();

        person = intent.getStringExtra("name");
       final int total =intent.getIntExtra("total",0);
        final String final_type = intent.getStringExtra("final_type");
       // Log.v("Hii", " received total as "+ total);

        final ArrayList<Word> words = new ArrayList<Word>();


        FirebaseDatabase.getInstance().getReference().child("record")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Word word = snapshot.getValue(Word.class);

                            if(word.getName().equals(person)) {
                                //Log.v("Hii","Found description as " + word.getDescription());
                                words.add(word);

                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        Log.v("Hii","Name in FFD is " +person);
        TextView text = (TextView) findViewById(R.id.texty);
        text.setText("Your transactions with " + person);


        TextView text2 = (TextView) findViewById(R.id.owe);
        TextView text3 = (TextView) findViewById(R.id.amt);

       // Log.v("Hii","total in FFD is     " +total);

       text2.setText(final_type);
        text3.setText(Integer.toString(total));

        //Log.v("Hii","total iiin FFD is " +total);

        FFDAdapter adapter = new FFDAdapter(this, words, R.color.category_friends);
        final ListView listView1 = (ListView) findViewById(R.id.list1);

        listView1.setAdapter(adapter);
        mProgressBar1.setVisibility(ProgressBar.INVISIBLE);


        //Pay Up Button

        //Log.v("Hii","Total in FFD is " + total);
        Button pay_up = (Button) findViewById(R.id.pay_up);

        pay_up.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                //Toast.makeText(FriendsFragmentDetail.this,"Click recorded",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(view.getContext(),SettleUp.class);
                intent1.putExtra("name",person);
                intent1.putExtra("total",total);
                intent1.putExtra("final_type", final_type);
                startActivity(intent1);

            }

        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
