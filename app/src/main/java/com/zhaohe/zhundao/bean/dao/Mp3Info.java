package com.zhaohe.zhundao.bean.dao;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/3/2 16:59
 */
public class Mp3Info implements Serializable{
    private static final long serialVersionUID = 7653446401224937583L;
    private String love;//喜爱的歌曲
    private long id; // 歌曲ID 3
    private String title; // 歌曲名称 0
    private String album; // 专辑 7
    private long albumId;//专辑ID 6
    private String displayName; //显示名称 4
    private String artist; // 歌手名称 2
    private long duration; // 歌曲时长 1
    private long size; // 歌曲大小 8
    private String url; // 歌曲路径 5
    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小
    private int position;//相对位置


    public Mp3Info(long id, String title, String album, long albumId,
                   String displayName, String artist, long duration, long size,
                   String url, String lrcTitle, String lrcSize,String love,int position) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.albumId = albumId;
        this.displayName = displayName;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.lrcTitle = lrcTitle;
        this.lrcSize = lrcSize;
        this.love=love;
        this.position=position;
    }

    @Override
    public String toString() {
        return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album
                + ", albumId=" + albumId + ", displayName=" + displayName
                + ", artist=" + artist + ", duration=" + duration + ", size="
                + size + ", url=" + url + ", lrcTitle=" + lrcTitle
                + ", lrcSize=" + lrcSize + "love"+love+"position"+position+"]";
    }
    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
