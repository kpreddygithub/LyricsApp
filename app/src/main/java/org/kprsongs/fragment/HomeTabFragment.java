package org.kprsongs.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.kprsongs.component.HomeViewerPageAdapter;
import org.kprsongs.component.SlidingTabLayout;
import org.kprsongs.glorytogod.R;

import java.util.List;

/**
 * author:K Purushotham Reddy
 * version:2.1.0
 */
public class HomeTabFragment extends Fragment implements View.OnClickListener {
    private List<String> titles;
    private ViewPager pager;
    private HomeViewerPageAdapter adapter;
    private SlidingTabLayout tabs;
    private ImageView songListIv;
    private ImageView authorIv;
    private ImageView favIv;
    private TextView favTv;
    private TextView authorTv;
    private TextView songListTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.home_tab_layout, container, false);
        songListIv = (ImageView) view.findViewById(R.id.song_list_iv);
        songListTv = (TextView) view.findViewById(R.id.song_list_tv);
        authorIv = (ImageView) view.findViewById(R.id.author_iv);
        authorTv = (TextView) view.findViewById(R.id.author_tv);
        favIv = (ImageView) view.findViewById(R.id.fav_iv);
        favTv = (TextView) view.findViewById(R.id.fav_tv);

        view.findViewById(R.id.layout_song_list).setOnClickListener(this);
        view.findViewById(R.id.layout_author).setOnClickListener(this);
        view.findViewById(R.id.layout_fav).setOnClickListener(this);

        fragmentTransaction(new SongsListFragment(), false);
/*        titles = Arrays.asList(getResources().getString(R.string.titles), getResources().getString(R.string.artists), getResources().getString(R.string.playlists));
        fragmentTransaction(new SongsListFragment(), false);
        adapter = new HomeViewerPageAdapter(getContext(), getChildFragmentManager(), titles);
        adapter.notifyDataSetChanged();

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.bright_foreground_material_dark);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);*/
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layout_song_list:
                songListIv.setImageResource(R.drawable.ic_pricelist_icon);
                songListTv.setTextColor(getResources().getColor(R.color.colorPrimary_1000));
                authorIv.setImageResource(R.drawable.ic_album_nofill);
                authorTv.setTextColor(getResources().getColor(R.color.liteGray));
                favIv.setImageResource(R.drawable.ic_favorite_gray);
                favTv.setTextColor(getResources().getColor(R.color.liteGray));
                fragmentTransaction(new SongsListFragment(), false);
                break;

            case R.id.layout_author:
//                frwdAnimIntent(HomeActivity.this, CategoryListActivity.class);
                songListIv.setImageResource(R.drawable.ic_pricelist_grey);
                songListTv.setTextColor(getResources().getColor(R.color.liteGray));
                authorIv.setImageResource(R.drawable.ic_album);
                authorTv.setTextColor(getResources().getColor(R.color.colorPrimary_1000));
                favIv.setImageResource(R.drawable.ic_favorite_gray);
                favTv.setTextColor(getResources().getColor(R.color.liteGray));
                fragmentTransaction(new AuthorListFragment(), false);
                break;

            case R.id.layout_fav:// TODO: 14-02-2018 for testing bypassing to Price drop Activity.
                songListIv.setImageResource(R.drawable.ic_pricelist_grey);
                songListTv.setTextColor(getResources().getColor(R.color.liteGray));
                authorIv.setImageResource(R.drawable.ic_album_nofill);
                authorTv.setTextColor(getResources().getColor(R.color.liteGray));
                favIv.setImageResource(R.drawable.ic_fav_filled);
                favTv.setTextColor(getResources().getColor(R.color.colorPrimary_1000));
                fragmentTransaction(new ServiceListFragment(), false);
                break;
        }

    }

    protected void fragmentTransaction(Fragment fragment, boolean allowStateLoss) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment, fragment.getClass().getSimpleName());
//        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss();
        } else
            fragmentTransaction.commit();
    }

}