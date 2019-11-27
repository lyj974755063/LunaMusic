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

    // the music now playing temp name
    private String currentPlayingName;

    // temp index
    private int currentPlayingIndex;

    // playing list
    private ArrayList<String> playList;

    // MediaPlayer
    private MediaPlayer mediaPlayer;

    private Intent broadcastIntent;

    public MusicService() {
        //bIsNewSong = true;
        currentPlayingName = "null";
        currentPlayingIndex = -1;
        broadcastIntent = new Intent();
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
       binder = new MyBinder();
       mediaPlayer = new MediaPlayer();

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

        // set path
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
        // broadcast action
        public final static int UPDATE_UI_PLAYING = 1;
        public final static int UPDATE_UI_STOP = 0;
        public final static int UPDATE_UI_PREVIOUS = -1;
        public final static int UPDATE_UI_NEXT = 2;

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        // when use this, make sure currPlayIndex > -1
        public boolean isSameList() {
            if (currentPlayingIndex != -1) {
                if (currentPlayingIndex < playList.size()) {
                    if (currentPlayingName.equals(playList.get(currentPlayingIndex))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        public int getCurrentPlayingIndex() {return currentPlayingIndex;}

        public int getPreparedIndex() {return preparedIndex;}

        public void iniPlayList(ArrayList<String> playList) {
            // initialize
            preparedIndex = 0;
            MusicService.this.playList = playList;

            if (currentPlayingIndex != -1){
                if (currentPlayingIndex < playList.size()) {
                    if (currentPlayingName.equals(playList.get(currentPlayingIndex))) {
                        initialUI(currentPlayingIndex);
                        preparedIndex = currentPlayingIndex;
                    }
                }
            }
            if (mediaPlayer.isPlaying()) {
                if (currentPlayingName.equals(playList.get(preparedIndex))) {
                    updateUI(UPDATE_UI_PLAYING);
                }
            }
        }

        private void updateUI(int state) {
            broadcastIntent.setAction("UPDATE_UI");
            broadcastIntent.putExtra("UI_STATE", state);
            sendBroadcast(broadcastIntent);
        }

        private void initialUI(int index) {
            broadcastIntent.setAction("INITIAL_UI");
            broadcastIntent.putExtra("UI_STATE", index);
            sendBroadcast(broadcastIntent);
        }

        /**
         * play or pause or startNew
         */
        public void handleMusic() {
            if (!currentPlayingName.equals(playList.get(preparedIndex))) {
                iniMediaPlayerFile(preparedIndex);
                mediaPlayer.start();
                updateUI(UPDATE_UI_PLAYING);
                currentPlayingName = playList.get(preparedIndex);
                currentPlayingIndex = preparedIndex;
            } else {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    updateUI(UPDATE_UI_PLAYING);
                } else {
                    mediaPlayer.pause();
                    updateUI(UPDATE_UI_STOP);
                }
            }
        }

        /**
         * pause
         */
        @Deprecated
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
                    ++preparedIndex;
                    if (isSameList()) {
                        if (mediaPlayer.isPlaying()) {
                            handleMusic();
                        }
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
                    --preparedIndex;
                    if (isSameList()) {
                        if (mediaPlayer.isPlaying()) {
                            handleMusic();
                        }
                    }
                }
            }
        }
    }

}
