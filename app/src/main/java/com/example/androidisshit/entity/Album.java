package com.example.androidisshit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private boolean addSongToAlbum(Song song) {
        if (checkCanAddTargetAlbum(song,this)) {
            this.albumSongs.add(song);
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<Album> getAllAlbums(List<Song> allSongs) {
        ArrayList<Album> tAlbums = new ArrayList<>();

        for (int i = 0; i < allSongs.size(); i++) {

            // move song[i] to tAlbum as a new album
            tAlbums.add(new Album(allSongs.get(i)));
            i++;
            if (!(i < allSongs.size()))
                break;

            for (int j = 0; j < tAlbums.size(); j++) {
                if (tAlbums.get(j).addSongToAlbum(allSongs.get(i))) {
                    break;
                }
            }
        }
        return tAlbums;
    }

    private static boolean checkCanAddTargetAlbum(Song song, Album target) {
        if (song!=null&&target!=null){
            if (target.getAlbumSongs().isEmpty()) {
                return true;
            } else {
                if (song.getAlbumTitle().equals(target.getAlbumTitle())) {
                    return true;
                } else {
                    return false;
                }
            }
        }else{
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

