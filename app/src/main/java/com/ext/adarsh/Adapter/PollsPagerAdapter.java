package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.TasksFragment;
import com.ext.adarsh.Fragment.mypolls;
import com.ext.adarsh.Fragment.polls;

public class PollsPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PollsPagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                polls tab1 = new polls();
                return tab1;
            case 1:
                mypolls tab2 = new mypolls();
                return tab2;

            default:
                return new TasksFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "POLLS";
            case 1:
                return "MY POLLS";
            default:
                return "POLLS";
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}