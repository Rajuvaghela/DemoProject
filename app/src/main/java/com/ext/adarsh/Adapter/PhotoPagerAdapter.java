package com.ext.adarsh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.AlbumFragment;
import com.ext.adarsh.Fragment.AllphotosFragment;
import com.ext.adarsh.Fragment.TasksFragment;


public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PhotoPagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AllphotosFragment tab1 = new AllphotosFragment();
                return tab1;
            case 1:
                AlbumFragment tab2 = new AlbumFragment();
                return tab2;

            default:
                return new TasksFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ALL PHOTOS";
            case 1:
                return "ALBUM";
            default:
                return "ALL PHOTOS";
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