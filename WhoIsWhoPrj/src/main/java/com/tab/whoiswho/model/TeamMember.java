package com.tab.whoiswho.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamMember implements Parcelable {
    private String mName;
    private String mJobTitle;
    private String mBiography;
    private String mImageURI;

    public TeamMember() {

    };

    public TeamMember(String name, String jobTitle, String biography, String imagePath) {
        mName = name;
        mJobTitle = jobTitle;
        mBiography = biography;
        mImageURI = imagePath;
    }

    private TeamMember(Parcel in) {
        mName = in.readString();
        mJobTitle = in.readString();
        mBiography = in.readString();
        mImageURI = in.readString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getJobTitle() {
        return mJobTitle;
    }

    public void setJobTitle(String jobTitle) {
        mJobTitle = jobTitle;
    }

    public String getBiography() {
        return mBiography;
    }

    public void setBiography(String biography) {
        mBiography = biography;
    }

    public String getImageURI() {
        return mImageURI;
    }

    public void setImageURI(String imageURI) {
        mImageURI = imageURI;
    }


    public static final Parcelable.Creator<TeamMember> CREATOR
            = new Parcelable.Creator<TeamMember>() {
        public TeamMember createFromParcel(Parcel in) {
            return new TeamMember(in);
        }

        public TeamMember[] newArray(int size) {
            return new TeamMember[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mJobTitle);
        parcel.writeString(mBiography);
        parcel.writeString(mImageURI);
    }


}
