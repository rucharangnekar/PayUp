package com.example.android.payup4;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    //private String[] mDrawerTitles;
    //private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;

    //private CharSequence mDrawerTitle;
    //private CharSequence mTitle;

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String mUsername;


    private FirebaseAuth mFirebasAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;


    //nav drawer
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private ImageButton profile;

    private static final String urlNavHeaderBg = "https://s-media-cache-ak0.pinimg.com/originals/dd/4d/60/dd4d60009fe992f067e9e0371e991115.jpg";
    private static final String urlProfileImg = "https://qph.ec.quoracdn.net/main-thumb-77867262-200-vploitlhhheaqdcoyagjyawoeovbfxqx.jpeg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Friends";
    private static final String TAG_ME= "Personal";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;


    private static final int RC_PHOTO_PICKER =  2;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();//main access point
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mFirebasAuth =  FirebaseAuth.getInstance();
        mFirebaseStorage= FirebaseStorage.getInstance().getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("profile-photos");


        //DRAWER

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        profile = (ImageButton) navHeader.findViewById(R.id.change_profile);

        Log.v("pay","profile is "+ profile);


        //final Button profile = (Button) findViewById(R.id.change_profile);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, profile);
                popup.getMenuInflater()
                        .inflate(R.menu.profile_picture, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();


                        if (item.getTitle().toString().equals("Choose from gallery")) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("images/jpeg");
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            startActivityForResult(
                                    Intent.createChooser(intent,
                                            "Complete action using"),
                                    RC_PHOTO_PICKER);

                            Log.v("pay", "passed " + RC_PHOTO_PICKER);
                        }
                            return true;
                        }

                });

                popup.show(); //showing popup menu

            }
        }); //closing the setOnClickListener method


        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }




//
//        // Find the view pager that will allow the user to swipe between fragments
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        // Create an adapter that knows which fragment should be shown on each page
//        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//
//        // Find the tab layout that shows the tabs
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        Log.v("Hii","setting up");
//        tabLayout.setupWithViewPager(viewPager);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //check if user is logged in. If not, show login screen

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null)
                {
                    //user signed in
                    OnSignedInInitialise(user.getDisplayName());
                }
                else
                {
                    //no user has logged in so use firebaseUI flow

                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false) // enabling it allows us to save the users credentials to keep them signed in
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);// RC is request code
                }
            }
        };


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.v("Pay","request code is "+ requestCode);
        if(requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }


        }


        else if(requestCode ==RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {
            Uri selectedImageuri = data.getData();
            StorageReference photoRef=mStorageReference.child(selectedImageuri.getLastPathSegment());

            String fileName = mUsername+".jpg";

            photoRef.putFile(selectedImageuri).addOnSuccessListener
                    (this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                }
                            }
                    );
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebasAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener !=null)
        {
            mFirebasAuth.removeAuthStateListener(mAuthStateListener);
        }
        //detachDatabaseReadListener();
       // mMessageAdapter.clear();
    }


    private void OnSignedInInitialise(String username){
        mUsername = username;
       // attachDatabaseReadListener();
    }

    private void onSignedOutCleanup()
    {
        mUsername= ANONYMOUS;
        //mMessageAdapter.clear();
        //detachDatabaseReadListener();
    }


    //REMEMBER TO ADD THESE 2 METHODS IN ALLLLLLL HAHAHA
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;

            case R.id.add_expense:
                Intent i = new Intent(this,Exp.class);
                Log.v("Hii","Entering add expense class");
                startActivity(i);
                return true;

            case R.id.add_transaction:
                Toast.makeText(this,"You clicked the button",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,AddTransaction.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Rucha Rangnekar");
        //txtWebsite.setText("www.payup.com");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);



    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    /*TRYING. REMOVE IT LATER.*/
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                FriendsFragment friendsFragment = new FriendsFragment();
                Log.v("Hii","return friendds");
                return friendsFragment;
            case 1:
                // photos
                ExpenseFragment exFragment = new ExpenseFragment();
                Log.v("Hii","return gro");

                return exFragment;
            case 2:
                // notifications fragment
                RecentsFragment notificationsFragment = new RecentsFragment();
                Log.v("Hii","return rece");

                return notificationsFragment;


            default:
                return new FriendsFragment();
        }
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();

        Log.v("Hii","Loading frag...");
        Log.v("Hii","tag is  "+ CURRENT_TAG);
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }



        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }



        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }









    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        Toast.makeText(MainActivity.this,"Home pressed",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_me:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ME;
                        Toast.makeText(MainActivity.this,"Personal pressed",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_reports:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PHOTOS;
                        Toast.makeText(MainActivity.this,"reports pressed",Toast.LENGTH_SHORT).show();


                        //startActivity(new Intent(MainActivity.this, Reports.class));
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;

                        PopupMenu popup = new PopupMenu(MainActivity.this, navigationView);
                        popup.getMenuInflater()
                                .inflate(R.menu.profile_picture, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


                                return true;
                            }
                        });

                        popup.show(); //showing popup menu

                break;
                    case R.id.nav_about_us:
                        Toast.makeText(MainActivity.this,"About pressed",Toast.LENGTH_SHORT).show();

                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //startActivity(new Intent(MainActivity.this, MainActivity.class));
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

}
