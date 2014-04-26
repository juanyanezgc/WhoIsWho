package com.tab.whoiswho.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamMember implements Parcelable {
    private int id;
    private String name;
    private String jobTitle;
    private String biography;
    private String imageURI;

    public TeamMember() {

    };

    public TeamMember(int id, String name, String jobTitle, String biography, String imageURI) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.biography = biography;
        this.imageURI = imageURI;
    }

    private TeamMember(Parcel in) {
        id =  in.readInt();
        name = in.readString();
        jobTitle = in.readString();
        biography = in.readString();
        imageURI = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
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
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(jobTitle);
        parcel.writeString(biography);
        parcel.writeString(imageURI);
    }


}
