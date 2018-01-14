package com.example.nghiatruong.musicapp;

/**
 * Created by Nghia Truong on 12/22/2017.
 */

public class Song {
    private String title;
    private String artist;
    private String album;
    private String path;
    private long duration;
    long albumID;
    private String composer;

    @Override
    public String toString()
    {
        return title;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getArtist(){
        return artist;
    }
    public void setArtist(String artist){
        this.artist=artist;
    }
    public String getAlbum(){
        return album;
    }
    public void setAlbum(String album){
        this.album=album;
    }

    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path=path;
    }
    public long getDuration(){
        return duration;
    }
    public void setDuration(long duration){
        this.duration=duration;
    }
    public long getAlbumID(){
        return albumID;
    }
    public void setAlbumID(long albumID){
        this.albumID=albumID;
    }
    public String getComposer(){
        return composer;
    }
    public void setComposer(String composer)
    {
        this.composer=composer;
    }
}
