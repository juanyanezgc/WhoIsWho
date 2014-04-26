package com.tab.whoiswho.model;

public class TeamMember {
    private String mName;
    private String mJobTitle;
    private String mBiography;
    private String mImageURI;


    public TeamMember(){

    };

    public TeamMember(String name, String jobTitle, String biography, String imagePath) {
        mName = name;
        mJobTitle = jobTitle;
        mBiography = biography;
        mImageURI = imagePath;
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
}
