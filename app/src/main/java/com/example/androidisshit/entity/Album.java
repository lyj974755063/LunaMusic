package com.example.androidisshit.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import com.example.androidisshit.utils.ColorUtils.MediaNotificationProcessor;

public class Album implements Serializable {
    private ArrayList<Song> albumSongs;
    private String albumTitle;
    private String albumArtist;
    private long albumId;

    private int primaryColor;
    private int secondColor;
    private boolean bIsTextLight;
    private int backgroundColor;

    public static ArrayList<Album> AllAlbums;

    /*
    public Album(List<Song> songs) {
        if (!songs.isEmpty()) {
            this.albumSongs = songs;
            albumArtist = albumSongs.get(0).getArtist();
            albumTitle = albumSongs.get(0).getAlbumTitle();
            albumId = albumSongs.get(0).getAlbumId();
        }
    }
     */

    public Album(Song song) {
        this.albumSongs = new ArrayList<>();
        albumSongs.add(song);
        albumArtist = albumSongs.get(0).getArtist();
        albumTitle = albumSongs.get(0).getAlbumTitle();
        albumId = albumSongs.get(0).getAlbumId();
        primaryColor = 0;
        secondColor = 0;
        bIsTextLight = false;
        backgroundColor = 0;
    }

    public ArrayList<Song> getAlbumSongs() {
        return albumSongs;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public static ArrayList<Album> getAllAlbums(ArrayList<Song> allSongs) {
        ArrayList<Album> tAlbums = new ArrayList<>();

        outer: for (int pS = 0; pS < allSongs.size(); pS++) {

            // move song[i] to tAlbum as a new album
            if (pS==0) {
                //System.out.println("MYFALG_ONCE");
                tAlbums.add(new Album(allSongs.get(pS)));
                pS++;
            }

            if (!(pS < allSongs.size()))
                break;

            //System.out.println("******Start*******");
            for (int pA = 0; pA < tAlbums.size(); pA++) {
                if (tAlbums.get(pA).addSongToAlbum(allSongs.get(pS))) {
                    continue outer;
                }
            }
            tAlbums.add(new Album(allSongs.get(pS)));
            //System.out.println("******END*******");
        }
        return tAlbums;
    }

    private boolean addSongToAlbum(Song song) {
        if (checkCanAddTargetAlbum(song,this)) {
            this.albumSongs.add(song);
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkCanAddTargetAlbum(Song song, Album target) {
        if (song!=null&&target!=null){
            if (target.getAlbumSongs().isEmpty()) {
                //System.out.println("MYFLAG_target.getAlbumSongs().isEmpty()_true");
                return true;
            } else {
                //System.out.println("MYFLAG_COMPARE<"+song.getAlbumTitle()+">AND<"+target.getAlbumTitle()+">");
                if (song.getAlbumTitle().equals(target.getAlbumTitle())) {
                    return true;
                } else {
                    return false;
                }
            }
        }else{
            //System.out.println("MYFLAG_song!=null&&target!=null_true");
            return false;
        }
    }

    public long getAlbumId() {
        return albumId;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getSecondColor() {
        return secondColor;
    }

    public int getBackgroundColor() { return backgroundColor; }

    public boolean isbIsTextLight() { return bIsTextLight; }

    public void calculateColors(Drawable drawable, @NonNull final CalculateListener calculateListener) {
        MediaNotificationProcessor processor = new MediaNotificationProcessor();
        processor.getPaletteAsync(drawable, new MediaNotificationProcessor.OnPaletteLoadedListener() {
            @Override
            public void onPaletteLoaded(MediaNotificationProcessor mediaNotificationProcessor) {
                primaryColor = mediaNotificationProcessor.getPrimaryTextColor();
                secondColor = mediaNotificationProcessor.getSecondaryTextColor();
                secondColor = ColorUtils.setAlphaComponent(secondColor, 191);
                backgroundColor = mediaNotificationProcessor.getBackgroundColor();
                bIsTextLight = ColorUtils.calculateLuminance(backgroundColor) > ColorUtils.calculateLuminance(primaryColor);
                calculateListener.doSomething();
                for (Song song : albumSongs) {
                    song.setColors(primaryColor,secondColor,backgroundColor,bIsTextLight);
                }
            }
        });
    }

    public interface CalculateListener {
        void doSomething();
    }

}

