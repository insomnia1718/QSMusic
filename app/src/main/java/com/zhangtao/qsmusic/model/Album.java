package com.zhangtao.qsmusic.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tao.zhang on 16-9-23.
 */

public class Album implements Serializable{
    public static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String artist;

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    private int songsCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

}
