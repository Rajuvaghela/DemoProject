package com.ext.adarsh.Activity.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ext.adarsh.Fragment.AlbumFragment;
import com.ext.adarsh.Fragment.AllphotosFragment;
import com.ext.adarsh.Fragment.TasksFragment;
import com.ext.adarsh.Utils.AppConstant;


public class OPhotoPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    String people_id = "";

    //Constructor to the class
    public OPhotoPagerAdapter(FragmentManager fm, String people_id) {
        super(fm);
        this.people_id = people_id;
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AppConstant.people_id_for_photos = people_id;
                OAllphotosFragment tab1 = new OAllphotosFragment();
                return tab1;
            case 1:
                AppConstant.people_id_for_photos = people_id;
                OAlbumFragment tab2 = new OAlbumFragment();
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