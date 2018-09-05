package com.br.bestrecipes.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private long id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    protected Step(Parcel in) {
        setId(in.readLong());
        setShortDescription(in.readString());
        setDescription(in.readString());
        setVideoUrl(in.readString());
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
        dest.writeString(getVideoUrl());
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
