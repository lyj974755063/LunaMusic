package com.example.androidisshit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.palette.graphics.Target;

public class Album implements Serializable {
    private List<Song> albumSongs;
    private String albumTitle;
    private String albumArtist;
    private long albumId;
    private int primaryColor;
    private int secondColor;

    public static List<Album> AllAlbums;

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

    public List<Song> getAlbumSongs() {
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

    public static List<Album> getAllAlbums(List<Song> allSongs) {
        List<Album> tAlbums = new ArrayList<>();

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

    public void calculateColors(Bitmap bitmap, @NonNull final CalculateListener calculateListener) {
        Palette.Builder pBuilder = new Palette.Builder(bitmap);
        /*
        Palette.Filter filter =new Palette.Filter() {
            @Override
            public boolean isAllowed(int rgb, @NonNull float[] hsl) {
                System.out.println(hsl[0]+","+hsl[1]+","+hsl[2]);
                if (hsl[1] <= 0.5 && hsl[2] >= 0.33) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        pBuilder.addFilter(filter);

        Target.Builder tBuilder = new Target.Builder();

        final Target STarget = tBuilder
                .setPopulationWeight(0.0f)
                .setSaturationWeight(1.0f)
                .setLightnessWeight(0.0f)
                .setExclusive(false)
                .setMinimumLightness(0.2f)
                .setTargetLightness(1.0f)
                .setMaximumLightness(1.0f)
                .setMinimumSaturation(0.1f)
                .setTargetSaturation(1.0f)
                .setMaximumSaturation(1.0f)
                .build();

        final Target LTarget = tBuilder
                .setPopulationWeight(0.0f)
                .setSaturationWeight(0.0f)
                .setLightnessWeight(1.0f)
                .setExclusive(false)
                .setMinimumLightness(0.1f)
                .setTargetLightness(1.0f)
                .setMaximumLightness(1.0f)
                .setMinimumSaturation(0.2f)
                .setTargetSaturation(1.0f)
                .setMaximumSaturation(1.0f)
                .build();
        */
        pBuilder
                .maximumColorCount(50)
                .clearFilters();

        pBuilder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int color1 = 0, color2 = 0;

                color1 = palette.getVibrantColor(Color.rgb(30,30,30));
                color2 = palette.getMutedColor(Color.GRAY);

                primaryColor = color1;
                secondColor = color2;
                calculateListener.doSomething(color1,color2);
            }
        });
    }

    public interface CalculateListener {
        void doSomething(@Nullable int color1, int color2);
    }

}

