package com.example.lunamusic.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.lunamusic.R;
import com.example.lunamusic.component.NowPlayingCoverView;
import com.example.lunamusic.component.VerticalTextView;
import com.example.lunamusic.entity.Song;
import com.example.lunamusic.utils.MusicUtils;

public class NowPlayingFragment extends Fragment {
    private String title;
    private String artist;
    private Long id;
    private Song song;

    public static NowPlayingFragment getInstance(int position, Song song) {
        NowPlayingFragment fragment = new NowPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        bundle.putSerializable("Song", song);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, null);

//        Window window = getActivity().getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Song song;
        if (getArguments() != null) {
            song = (Song)getArguments().getSerializable("Song");
            title = song.getTitle();
            artist = song.getArtist();
            id = song.getAlbumId();
        } else {
            System.err.println("getArguments() in NPFragment return null, can't get data of song!");
            song = new Song();
        }

        if (!song.isbIsTextLight()) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ConstraintLayout constraintLayout = view.findViewById(R.id.nowPlay_background);
        constraintLayout.setBackgroundColor(song.getBackgroundColor());

        NowPlayingCoverView nowPlayingCoverView = view.findViewById(R.id.nowPlay_cover);
        nowPlayingCoverView.setCoverUri(MusicUtils.getArtUri(id));

        VerticalTextView verticalTextView = view.findViewById(R.id.nowPlay_title);
        verticalTextView.setText(title.toUpperCase());
        verticalTextView.setTextColor(song.getSecondColor());

        TextView artistTextView = view.findViewById(R.id.nowPlay_artist);
        artistTextView.setTextColor(song.getSecondColor());
        artistTextView.setText(song.getArtist()+"の世界");

        Button button = getActivity().findViewById(R.id.nowPlay_button_start);
        button.setTextColor(song.getPrimaryColor());

        // Return
        return view;
    }
}
