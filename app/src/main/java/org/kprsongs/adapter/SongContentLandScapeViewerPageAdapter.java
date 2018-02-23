package org.kprsongs.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.kprsongs.CommonConstants;
import org.kprsongs.SongsApplication;
import org.kprsongs.dao.AuthorSongDao;
import org.kprsongs.dao.SongDao;
import org.kprsongs.domain.AuthorSong;
import org.kprsongs.domain.Song;
import org.kprsongs.fragment.SongContentLandscapeViewFragment;

import java.util.List;

/**
 * author:K Purushotham Reddy
 * version:2.1.0
 */
public class SongContentLandScapeViewerPageAdapter extends FragmentStatePagerAdapter
{
    private String title;
    private SongsApplication application = new SongsApplication();
    private SongDao songDao = new SongDao(application.getContext());
    private AuthorSongDao authorSongDao = new AuthorSongDao(application.getContext());
    private List<String> contents;
    private AuthorSong authorSong;
    private Song song;


    public SongContentLandScapeViewerPageAdapter(FragmentManager fragmentManager, String title)
    {
        super(fragmentManager);
        this.title = title;
        initSetUp();
    }

    public void initSetUp()
    {
        song = songDao.findContentsByTitle(title);
        contents = song.getContents();
        authorSong = authorSongDao.findByTitle(title);
    }

    @Override
    public Fragment getItem(int position)
    {
        SongContentLandscapeViewFragment songContentLandscapeViewFragment = new SongContentLandscapeViewFragment();
        Bundle bundle = new Bundle();
        String content = contents.get(position);
        bundle.putString("content", content);
        bundle.putString(CommonConstants.TITLE_KEY, title);
        bundle.putString("authorName", authorSong.getAuthor().getDisplayName());
        bundle.putString("position", String.valueOf(position));
        bundle.putString("size", String.valueOf(contents.size()));
        bundle.putString("chord", song.getChord());
        songContentLandscapeViewFragment.setArguments(bundle);
        return songContentLandscapeViewFragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "";
    }

    @Override
    public int getCount()
    {
        return contents.size();
    }
}
