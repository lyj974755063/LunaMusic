package com.example.androidisshit.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {

    private MyBinder binder;

    // prepared music index
    private int preparedIndex;

    // is current index moved
    private boolean bIsNewSong;

    private String currentPlayingName;

    // playing list
    private ArrayList<String> playList;

    // MediaPlayer
    private MediaPlayer mediaPlayer;

    public MusicService() {
        //bIsNewSong = true;
        currentPlayingName = "null";
        System.out.println("MYFLAG_MusicService()_run_once");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MYFLAG_onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        System.out.println("MYFLAG_onCreate");
        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println("MYFLAG_onRebind");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MYFLAG_onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("MYFLAG_start_bind_service");

        // initialize
//        currentPlayingIndex = 0;
//        playList = new ArrayList<String>();
       binder = new MyBinder();
       mediaPlayer = new MediaPlayer();

//        if (intent != null) {
//            for (String t : Objects.requireNonNull(intent.getStringArrayListExtra("PlayList"))) {
//                playList.add(t);
//                System.out.println("MYFLAG_intent_content_get_"+t);
//            }
//        } else {
//            System.err.println("MYFLAG_intent null!");
//        }
//        iniMediaPlayerFile(currentPlayingIndex);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    binder.nextMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // return
        return binder;
    }

    private void iniMediaPlayerFile(int index) {

        //获取文件路径
        try {
            System.out.println("MYFLAG_setDataSource_"+playList.get(index));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playList.get(index));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class MyBinder extends Binder {

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void iniPlayList(ArrayList<String> playList) {
            // initialize
            preparedIndex = 0;
            MusicService.this.playList = playList;
            //bIsNewSong = true;
            //iniMediaPlayerFile(preparedIndex);
        }

        /**
         * play
         */
        public void playMusic() {
            if (!currentPlayingName.equals(playList.get(preparedIndex))) {
                iniMediaPlayerFile(preparedIndex);
                mediaPlayer.start();
                //bIsNewSong = false;
                currentPlayingName = playList.get(preparedIndex);
            } else {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        }

        /**
         * pause
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
                iniMediaPlayerFile(preparedIndex);
            }
        }

        /**
         * close
         */
        public void closeMedia() {
            if (mediaPlayer != null) {
                //mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        /**
         * next
         */
        public void nextMusic() {
            if (mediaPlayer != null && preparedIndex >=0 && preparedIndex < playList.size()) {
                if ((preparedIndex + 1) < playList.size()) {
                    mediaPlayer.reset();
                    ++preparedIndex;
                    if (mediaPlayer.isPlaying()) {
                        playMusic();
                    }
                }
            }
        }

        /**
         * precious
         */
        public void preciousMusic() {
            if (mediaPlayer != null && preparedIndex >=0 && preparedIndex < playList.size()) {
                if ((preparedIndex - 1) >= 0) {
                    mediaPlayer.reset();
                    --preparedIndex;
                    if (mediaPlayer.isPlaying()) {
                        playMusic();
                    }
                }
            }
        }
    }

}
