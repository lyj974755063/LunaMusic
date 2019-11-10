package com.example.androidisshit.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MusicService extends Service {

    private MyBinder binder;

    // current playing music
    int currentPlayingIndex;

    // playing list
    ArrayList<String> playList;

    // MediaPlayer
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // initialize
        currentPlayingIndex = 0;
        playList = new ArrayList<String>();
        binder = new MyBinder();
        mediaPlayer = new MediaPlayer();

        if (intent != null) {
            for (String t : Objects.requireNonNull(intent.getStringArrayListExtra("PlayList"))) {
                playList.add(t);
                System.out.println("MYFLAG_intent_content_get_"+t);
            }
        } else {
            System.err.println("MYFLAG_intent null!");
        }
        iniMediaPlayerFile(currentPlayingIndex);

        // return
        return binder;
    }

    private void iniMediaPlayerFile(int index) {
        //获取文件路径
        try {
            System.out.println("MYFLAG_setDataSource_"+playList.get(index));
            mediaPlayer.setDataSource(playList.get(index));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    mBinder.nextMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/
    }

    public class MyBinder extends Binder {

        /**
         * play
         */
        public void playMusic() {
            if (!mediaPlayer.isPlaying()) {
                System.out.println("MYFLAG_do_play");
                mediaPlayer.start();
            } else {
                System.out.println("MYFLAG_play_error");
            }
        }

        /**
         * stop
         */
        public void pauseMusic() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        /**
         * reset
         */
        public void resetMusic() {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                iniMediaPlayerFile(currentPlayingIndex);
            }
        }

        /**
         * close
         */
        public void closeMedia() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        /**
         * next
         */
        public void nextMusic() {
            if (mediaPlayer != null && currentPlayingIndex >=0 && currentPlayingIndex < playList.size()) {
                if ((currentPlayingIndex + 1) < playList.size()) {
                    mediaPlayer.reset();
                    iniMediaPlayerFile(++currentPlayingIndex);
                    playMusic();
                }
            }
        }

        /**
         * last
         */
        public void preciousMusic() {
            if (mediaPlayer != null && currentPlayingIndex >=0 && currentPlayingIndex < playList.size()) {
                if ((currentPlayingIndex - 1) >= 0) {
                    mediaPlayer.reset();
                    iniMediaPlayerFile(--currentPlayingIndex);
                    playMusic();
                }
            }
        }
    }

}
