package org.kprsongs.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.kprsongs.fragment.AuthorListFragment;
import org.kprsongs.fragment.ServiceListFragment;
import org.kprsongs.fragment.SongsListFragment;

import java.util.List;

/**
 *  Author: K Purushotham Reddy.
 *  version: 1.0.0
 */
public class HomeViewerPageAdapter extends FragmentPagerAdapter
{

    //CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when HomeViewerPageAdapter is created
    //int NumbOfTabs; // Store the number of tabs, this will also be passed when the HomeViewerPageAdapter is created

    private List<String> titles;


    public HomeViewerPageAdapter(FragmentManager fragmentManager, List<String> titles)
    {
        super(fragmentManager);
        this.titles = titles;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {

        switch (position) {
            case 0:
                return new SongsListFragment();
            case 1:
                return new AuthorListFragment();
            case 2:
                return new ServiceListFragment();
        }
        return null;
    }



    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles.get(position);
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount()
    {
        return titles.size();
    }
}

