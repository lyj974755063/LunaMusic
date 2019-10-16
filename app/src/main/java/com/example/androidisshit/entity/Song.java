package com.example.androidisshit.entity;

import java.util.List;

public class Song {
    private String title;//歌曲名
    private String artist;//歌手
    private long size;//文件大小
    private int duration;//歌曲时间长度
    private String path;//歌曲路径
    private long  albumId;//专辑图片id
    private String albumTitle;//专辑名
    private long id;//歌曲id

    public static List<Song> allSongs;

    public Song(){};

    public long getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(long albumId)
    {
        this.albumId = albumId;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
}
