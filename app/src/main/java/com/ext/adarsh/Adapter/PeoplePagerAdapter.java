package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.MyContact;
import com.ext.adarsh.Fragment.PeopleFragment;
import com.ext.adarsh.Fragment.PeopleGroupsFragment;
import com.ext.adarsh.Fragment.favourite;

public class PeoplePagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PeoplePagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PeopleFragment tab1 = new PeopleFragment();
                return tab1;
            case 1:
                favourite tab2 = new favourite();
                return tab2;
            case 2:
                MyContact tab3 = new MyContact();
                return tab3;
            case 3:
                PeopleGroupsFragment tab4 = new PeopleGroupsFragment();
                return tab4;

            default:
                return new PeopleFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PEOPLE";
            case 1:
                return "FAVOURITE";
            case 2:
                return "MY CONTACT";
            case 3:
                return "GROUPS";

            default:
                return "People";
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}