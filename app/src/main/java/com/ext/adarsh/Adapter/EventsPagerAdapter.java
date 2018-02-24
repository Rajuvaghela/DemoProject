package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.MyEvents;
import com.ext.adarsh.Fragment.PastEvents;
import com.ext.adarsh.Fragment.UpcomingEvents;

public class EventsPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public EventsPagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                UpcomingEvents tab1 = new UpcomingEvents();
                return tab1;
            case 1:
                PastEvents tab2 = new PastEvents();
                return tab2;
            case 2:
                MyEvents tab3 = new MyEvents();
                return tab3;

            default:
                return new UpcomingEvents();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Upcoming";
            case 1:
                return "Past Events";
            case 2:
                return "My Events";

            default:
                return "Upcoming";
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