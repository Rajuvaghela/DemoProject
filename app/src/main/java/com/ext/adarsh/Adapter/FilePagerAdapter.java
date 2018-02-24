package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.MyContact;
import com.ext.adarsh.Fragment.MyDrive;
import com.ext.adarsh.Fragment.PeopleFragment;
import com.ext.adarsh.Fragment.PeopleGroupsFragment;
import com.ext.adarsh.Fragment.RecentFileFragments;
import com.ext.adarsh.Fragment.ShareWithMeFragments;
import com.ext.adarsh.Fragment.favourite;

public class FilePagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public FilePagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                MyDrive tab1 = new MyDrive();
                return tab1;
            case 1:
                RecentFileFragments tab2 = new RecentFileFragments();
                return tab2;
            case 2:
                ShareWithMeFragments tab3 = new ShareWithMeFragments();
                return tab3;
            default:
                return new MyDrive();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "My Drive";
            case 1:
                return "Recent Files";
            case 2:
                return "Share With Me";

            default:
                return "My Drive";
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}