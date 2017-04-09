package com.example.android.payup4;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTransaction extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private String owed_type;
    private String type_owe= "You owe";
    private String type_owed = "You are owed";
    private String type_split = "Split equally";

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;
    public ChildEventListener mChildEventListener;

    private String mUsername;

    public FirebaseAuth mFirebasAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("record");
        mFirebasAuth =  FirebaseAuth.getInstance();
        FirebaseUser user = mFirebasAuth.getCurrentUser();
        if(user !=null)
        {
            //user signed in
           mUsername=user.getDisplayName();
        }
        Log.v("Hii","User is" + mUsername);
        final Button popup_menu=(Button) findViewById(R.id.you_menu);

        final StringBuilder  pop_up_value_selected = new StringBuilder("Paid by you and split equally");
        popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddTransaction.this, popup_menu);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(AddTransaction.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        pop_up_value_selected.replace(0,pop_up_value_selected.capacity(),
                                item.getTitle().toString());

                        //pop_up_value_selected = item.getTitle().toString();
                        popup_menu.setText(pop_up_value_selected);

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        Button save = (Button) findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                //Toast.makeText(FriendsFragmentDetail.this,"Click recorded",Toast.LENGTH_SHORT).show();

                EditText amount1 = (EditText) findViewById(R.id.amount_to_pay);
                String a = amount1.getText().toString();

                EditText person = (EditText) findViewById(R.id.person);

                amount1.setText(a);

                EditText description = (EditText) findViewById(R.id.description);

                String desc= description.getText().toString();
                String per = person.getText().toString();
                String type="";

                if(pop_up_value_selected.toString().equals("They owe the full amount"))
                {
                    owed_type=type_owed;
                }

                else if(pop_up_value_selected.toString().equals("You owe the full amount"))
                {
                    owed_type=type_owe;
                }

                else
                {
                    owed_type=type_split;
                }

                Word word = new Word(per,a,owed_type,desc,mUsername);

                mMessagesDatabaseReference.push().setValue(word);

                Intent intent1 = new Intent(view.getContext(),MainActivity.class);
                //intent1.putExtra("amount",a);

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
