package org.kprsongs.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import org.kprsongs.fragment.AuthorListFragment;
import org.kprsongs.fragment.ServiceListFragment;
import org.kprsongs.fragment.SongsListFragment;
import org.kprsongs.glorytogod.R;

import java.util.List;

/**
 * Author: K Purushotham Reddy.
 * version: 1.0.0
 */
public class HomeViewerPageAdapter extends FragmentPagerAdapter {
    private Context context;
    int[] drawables = {R.drawable.ic_price_list, R.drawable.ic_home, R.drawable.ic_fav};
    //CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when HomeViewerPageAdapter is created
    //int NumbOfTabs; // Store the number of tabs, this will also be passed when the HomeViewerPageAdapter is created

    private List<String> titles;
    Drawable myDrawable;

    public HomeViewerPageAdapter(Context context, FragmentManager fragmentManager, List<String> titles) {
        super(fragmentManager);
        this.context = context;
        this.titles = titles;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

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
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb = new SpannableStringBuilder("    "+titles.get(position)); // space added before text for convenience
        myDrawable = context.getResources().getDrawable(R.drawable.ic_price_list);
        myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
//        return titles.get(position);
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return titles.size();
    }
}

