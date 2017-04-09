package com.example.android.payup4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Exp extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private String owed_type;
    private String paid_by;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;
    public ChildEventListener mChildEventListener;

    private String mUsername;

    public FirebaseAuth mFirebasAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsername = ANONYMOUS;
        Log.v("Hii","in aaaadding expense");

        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("expense");
        mFirebasAuth =  FirebaseAuth.getInstance();
        FirebaseUser user = mFirebasAuth.getCurrentUser();
        if(user !=null)
        {
            //user signed in
            mUsername=user.getDisplayName();
        }

        Log.v("Hii","in adding expense");
        Log.v("Hii","User is" + mUsername);

        final Button expense_type=(Button) findViewById(R.id.cash_menu);
        final StringBuilder  pop_up_value_selected = new StringBuilder("Cash");
        expense_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Exp.this, expense_type);
                popup.getMenuInflater()
                        .inflate(R.menu.paid_by_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Exp.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        pop_up_value_selected.replace(0,pop_up_value_selected.capacity(),
                                item.getTitle().toString());

                        //pop_up_value_selected = item.getTitle().toString();
                        expense_type.setText(pop_up_value_selected);

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method



        final Button rec=(Button) findViewById(R.id.sent_menu);
        final StringBuilder  pop_up_value_selected1 = new StringBuilder("Sent");
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup1 = new PopupMenu(Exp.this, rec);
                popup1.getMenuInflater()
                        .inflate(R.menu.sent_menu, popup1.getMenu());

                //registering popup with OnMenuItemClickListener
                popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Exp.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        pop_up_value_selected1.replace(0,pop_up_value_selected1.capacity(),
                                item.getTitle().toString());

                        //pop_up_value_selected1 = item.getTitle().toString();
                        rec.setText(pop_up_value_selected1);

                        return true;
                    }
                });

                popup1.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        Button save = (Button) findViewById(R.id.add_expense);

        Log.v("Hii","Final values are " + pop_up_value_selected.toString() + " and " + pop_up_value_selected1);

        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                Log.v("Hii","you saved the expense(maybe)");

                EditText amount1 = (EditText) findViewById(R.id.exp_amount_to_pay);
                String a = amount1.getText().toString();
                amount1.setText(a);

                EditText description = (EditText) findViewById(R.id.exp_desc);
                String desc= description.getText().toString();

                String paid_how = pop_up_value_selected.toString();
                String sent_rec = pop_up_value_selected1.toString();

                Log.v("Hii","Adding " + desc);
                Log.v("Hii","Adding " + mUsername);
                Log.v("Hii","Adding " + paid_how);
                Log.v("Hii","Adding " + a);
                Log.v("Hii","Adding " + sent_rec);


                Personal expense = new Personal(desc,mUsername,paid_how,a,sent_rec);

                mMessagesDatabaseReference.push().setValue(expense);

                Intent intent1 = new Intent(view.getContext(),MainActivity.class);
                //intent1.putExtra("amount",a);
                Toast.makeText(Exp.this,"Expense recorded",Toast.LENGTH_SHORT).show();


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
