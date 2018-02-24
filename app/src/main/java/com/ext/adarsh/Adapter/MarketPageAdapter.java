package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 01-11-2017.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.AllKnowledgeFragment;
import com.ext.adarsh.Fragment.MarketBuyingFragment;
import com.ext.adarsh.Fragment.MarketMyAddFragment;
import com.ext.adarsh.Fragment.MarketSellingFragment;
import com.ext.adarsh.Fragment.PopularKnowledge;
import com.ext.adarsh.Fragment.RecentyAddedKnowledge;
import com.ext.adarsh.Fragment.UpcomingEvents;

public class MarketPageAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public MarketPageAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                MarketSellingFragment tab1 = new MarketSellingFragment();
                return tab1;
            case 1:
                MarketBuyingFragment tab2 = new MarketBuyingFragment();
                return tab2;
            case 2:
                MarketMyAddFragment tab3 = new MarketMyAddFragment();
                return tab3;

            default:
                return new UpcomingEvents();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SELLING";
            case 1:
                return "BUYING";
            case 2:
                return "MY ADS";

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
