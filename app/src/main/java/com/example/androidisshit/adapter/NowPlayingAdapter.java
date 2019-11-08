package com.example.androidisshit.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.androidisshit.entity.Album;
import com.example.androidisshit.entity.Song;
import com.example.androidisshit.fragment.NowPlayingFragment;

import java.util.ArrayList;

public class NowPlayingAdapter extends FragmentPagerAdapter {
    ArrayList<Song> playList;

    public NowPlayingAdapter(@NonNull FragmentManager fm, int behavior, ArrayList songs) {
        super(fm, behavior);
        playList = songs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return NowPlayingFragment.getInstance(position, playList.get(position));
    }

    @Override
    public int getCount() {
        return playList.size();
    }
}
