package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.Approvals;
import com.ext.adarsh.Fragment.TasksFragment;

public class TaskPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public TaskPagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                TasksFragment tab1 = new TasksFragment();
                return tab1;
            case 1:
                Approvals tab2 = new Approvals();
                return tab2;

            default:
                return new TasksFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TASKS";
            default:
                return "TASKS";
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}