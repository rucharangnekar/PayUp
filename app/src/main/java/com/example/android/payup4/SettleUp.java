package com.example.android.payup4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettleUp extends AppCompatActivity {

    private String type_owe= "You owe";
    private String type_owed = "You are owed";
    public static final String ANONYMOUS = "anonymous";
    private String type_pay= "You paid";
    private String type_paid = "You were paid";
    private String final_type;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;
    public ChildEventListener mChildEventListener;

    private String mUsername;

    public FirebaseAuth mFirebasAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);

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

        EditText amount = (EditText) findViewById(R.id.amount_to_settle);
        TextView pay_names = (TextView) findViewById(R.id.pay_names);

        Intent intent = getIntent();
        int total = intent.getIntExtra("total",0);
        String final_type = intent.getStringExtra("final_type");
        Log.v("Hii"," in settle up type is " + final_type);
        final String person = intent.getStringExtra("name");
        final String final_type1;
        Log.v("Hii","total found in SettleUp.java is "+ total);
        amount.setText(Integer.toString(total));


        if(final_type.equals(type_owe))
        {
            Log.v("Hii"," in type owe");

            final_type1=type_pay;
            pay_names.setText("You paid " + person);
        }
        else
        {
            Log.v("Hii"," in type owed ");

            final_type1= type_paid;
            pay_names.setText(person+" paid You");
        }

        Button save = (Button) findViewById(R.id.save);

        Log.v("Hii","total condition done");
        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                //Toast.makeText(FriendsFragmentDetail.this,"Click recorded",Toast.LENGTH_SHORT).show();

                EditText amount1 = (EditText) findViewById(R.id.amount_to_settle);
                String a = amount1.getText().toString();

                //add to Recents that so much was paid

                amount1.setText(a);

                Word word = new Word(person,a,final_type1,"No description added",mUsername);

                mMessagesDatabaseReference.push().setValue(word);


                Toast.makeText(SettleUp.this,"Settlement noted",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(view.getContext(),MainActivity.class);
                //intent1.putExtra("amount",a);

                startActivity(intent1);

            }

        });








    }
}
