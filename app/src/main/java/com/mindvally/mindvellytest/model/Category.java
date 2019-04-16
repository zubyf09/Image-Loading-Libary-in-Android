package com.mindvally.mindvellytest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Category implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo_count")
    @Expose
    private int photoCount;

    @SerializedName("download")
    @Expose
    private String download;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getPhotoCount()
    {
        return photoCount;
    }

    public void setPhotoCount(int photoCount)
    {
        this.photoCount = photoCount;
    }

    public String getDownload()
    {
        return download;
    }

    public void setDownload(String download)
    {
        this.download = download;
    }


}