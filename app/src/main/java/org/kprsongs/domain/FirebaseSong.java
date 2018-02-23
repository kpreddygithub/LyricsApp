package org.kprsongs.domain;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by K Purushotham Reddy on 4/11/2017.
 */

public class FirebaseSong {
    private int id;
    private int songBookId;
    private String title;
    private String alternateTitle;
    private String lyrics;
    private String songNumber;
    private String searchTitle;
    private String searchLyrics;
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongBookId() {
        return songBookId;
    }

    public void setSongBookId(int songBookId) {
        this.songBookId = songBookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlternateTitle() {
        return alternateTitle;
    }

    public void setAlternateTitle(String alternateTitle) {
        this.alternateTitle = alternateTitle;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }


    public String getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(String songNumber) {
        this.songNumber = songNumber;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchLyrics() {
        return searchLyrics;
    }

    public void setSearchLyrics(String searchLyrics) {
        this.searchLyrics = searchLyrics;
    }

    @Override
    public String toString() {
        return "FirebaseSong{" +
                "id=" + id +
                ", songBookId=" + songBookId +
                ", title='" + title + '\'' +
                ", alternateTitle='" + alternateTitle + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", songNumber='" + songNumber + '\'' +
                ", searchTitle='" + searchTitle + '\'' +
                ", searchLyrics='" + searchLyrics + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
