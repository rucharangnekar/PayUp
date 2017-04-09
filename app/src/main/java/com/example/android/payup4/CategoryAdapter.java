package com.example.android.payup4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {

        super(fm);
        mContext = context;
    }



    @Override
    public Fragment getItem(int position) {


        if (position == 0) {

            Log.v("Hii","friendsss adapter made");
            return new FriendsFragment();
        } else if (position == 1) {
            Log.v("Hii","groupppp adapter made");

            return new ExpenseFragment();
        } else{
            Log.v("Hii","recentssss adapter made");

            return new RecentsFragment();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            Log.v("Hii","Friends title taken");
            return mContext.getString(R.string.category_friends);
        } else if (position == 1) {

            Log.v("Hii","Personal title taken");

            return mContext.getString(R.string.category_personal);
        } else {

            Log.v("Hii","Recents title taken");

            return mContext.getString(R.string.category_recents);
        }

    }



    @Override
    public int getCount() {

        //Log.v("Hii","Took count");

        return 3;
    }
}
