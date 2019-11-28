package com.example.lunamusic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.lunamusic.entity.Song;
import com.example.lunamusic.fragment.NowPlayingFragment;

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
