package com.tab.whoiswho.logic;

import android.content.Context;

import java.io.File;

/**
 * Manages all the operations related to image storage
 */
public class FileManager {

    private static final String CACHE_FOLDER_NAME = "WhoIsWho";

    /**
     * Cache dir for save the images in
     */
    private File mCacheDir;


    /**
     * Creates a new FileManager
     *
     * @param context
     */
    public FileManager(Context context) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(android.os.Environment.getExternalStorageDirectory(), CACHE_FOLDER_NAME);
        } else {
            mCacheDir = context.getCacheDir();
        }

        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    /**
     * Gets the file for an image
     *
     * @param teamMemberID Identifier of the image
     */
    public File getImageFile(int teamMemberID) {
        String imageName = teamMemberID + ".jpg";
        File imageFile = new File(mCacheDir, imageName);
        return imageFile;
    }


    /**
     * Deletes a cache image file
     *
     * @param teamMemberID Identifier of the image
     */
    public void deleteImageFile(int teamMemberID) {
        File imageFile = getImageFile(teamMemberID);
        imageFile.delete();
    }

    /**
     * Deletes all the cache images files
     */
    public void deleteImageFiles() {
        File[] files = mCacheDir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}
