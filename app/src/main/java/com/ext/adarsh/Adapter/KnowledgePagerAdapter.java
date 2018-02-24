package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.AllKnowledgeFragment;
import com.ext.adarsh.Fragment.PopularKnowledge;
import com.ext.adarsh.Fragment.RecentyAddedKnowledge;
import com.ext.adarsh.Fragment.UpcomingEvents;

public class KnowledgePagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public KnowledgePagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AllKnowledgeFragment tab1 = new AllKnowledgeFragment();
                return tab1;
            case 1:
                PopularKnowledge tab2 = new PopularKnowledge();
                return tab2;
            case 2:
                RecentyAddedKnowledge tab3 = new RecentyAddedKnowledge();
                return tab3;

            default:
                return new UpcomingEvents();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Popular";
            case 2:
                return "Recently Added";

            default:
                return "All";
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