package com.br.bestrecipes.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private long id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailUrl;

    protected Step(Parcel in) {
        setId(in.readLong());
        setShortDescription(in.readString());
        setDescription(in.readString());
        setVideoURL(in.readString());
        setThumbnailUrl(in.readString());
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getShortDescription());
        dest.writeString(getDescription());
        dest.writeString(getVideoURL());
        dest.writeString(getThumbnailUrl());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
