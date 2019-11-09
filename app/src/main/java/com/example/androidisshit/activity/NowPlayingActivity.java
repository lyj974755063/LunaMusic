package com.example.androidisshit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.androidisshit.adapter.NowPlayingAdapter;
import com.example.androidisshit.component.NowPlayingCoverView;
import com.example.androidisshit.component.ParallaxPagerTransformer;
import com.example.androidisshit.component.VerticalTextView;
import com.example.androidisshit.entity.Album;
import com.example.androidisshit.entity.Song;
import com.example.androidisshit.utils.MusicUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidisshit.R;

import java.util.ArrayList;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class NowPlayingActivity extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Intent intent=getIntent();
        ArrayList<Song> playList = (ArrayList<Song>) intent.getSerializableExtra("playList");

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = findViewById(R.id.nowPlay_viewPager);
        NowPlayingAdapter adapter = new NowPlayingAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, playList);
//        viewPager.setPadding(-64,0,64,0);
        //viewPager.setClipToPadding(false);
        //viewPager.setPageMargin(30);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(true,
                new ParallaxPagerTransformer()
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_title,2f,2f))
                        .addViewToParallax(new ParallaxPagerTransformer.ParallaxTransformInformation(R.id.nowPlay_artist,3f,3f)));

        startButton = findViewById(R.id.nowPlay_button_start);
    }
}
