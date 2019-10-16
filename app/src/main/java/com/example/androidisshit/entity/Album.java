package com.example.androidisshit.entity;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private List<Song> albumSongs;
    private String albumTitle;
    private String albumArtist;
    private long albumId;

    public static List<Album> AllAlbums;

    public Album(List<Song> songs) {
        if (!songs.isEmpty()) {
            this.albumSongs = songs;
            albumArtist = albumSongs.get(0).getArtist();
            albumTitle = albumSongs.get(0).getAlbumTitle();
            albumId = albumSongs.get(0).getAlbumId();
        }
    }

    public Album(Song songs) {
        this.albumSongs = new ArrayList<>();
        albumSongs.add(songs);
        albumArtist = albumSongs.get(0).getArtist();
        albumTitle = albumSongs.get(0).getAlbumTitle();
        albumId = albumSongs.get(0).getAlbumId();
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
}
