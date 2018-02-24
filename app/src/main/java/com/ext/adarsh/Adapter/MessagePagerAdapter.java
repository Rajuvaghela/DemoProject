package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.InboxFragment;
import com.ext.adarsh.Fragment.MyContact;
import com.ext.adarsh.Fragment.PeopleFragment;
import com.ext.adarsh.Fragment.PeopleGroupsFragment;
import com.ext.adarsh.Fragment.SentFragment;
import com.ext.adarsh.Fragment.StarredFragment;
import com.ext.adarsh.Fragment.TrashFragment;
import com.ext.adarsh.Fragment.favourite;

public class MessagePagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public MessagePagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {

             case 0:
                InboxFragment tab1 = new InboxFragment();
                return tab1;
            case 1:
                //InboxFragment tab2 = new InboxFragment();
                //return tab2;
                SentFragment tab2 = new SentFragment();
                return tab2;
            case 2:
               // InboxFragment tab3 = new InboxFragment();
               // return tab3;
               TrashFragment tab3 = new TrashFragment();
                return tab3;
            case 3:
               // InboxFragment tab4 = new InboxFragment();
                //return tab4;
                StarredFragment tab4 = new StarredFragment();
                return tab4;
            default:
                return new InboxFragment();

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "INBOX";
            case 1:
                return "SENT";
            case 2:
                return "TRASH";
            case 3:
                return "STARRED";

            default:
                return "INBOX";
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