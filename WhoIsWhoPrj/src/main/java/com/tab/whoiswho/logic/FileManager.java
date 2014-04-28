package com.tab.whoiswho.logic;

import android.content.Context;

import java.io.File;

public class FileManager {

    private static final String CACHE_FOLDER_NAME =  "WhoIsWho";

    private File mCacheDir;

    public FileManager(Context context) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(android.os.Environment.getExternalStorageDirectory(), CACHE_FOLDER_NAME );
        } else {
            mCacheDir = context.getCacheDir();
        }

        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    public File getImageFile(int teamMemberID) {
        String imageName = teamMemberID + ".jpg";
        File imageFile = new File(mCacheDir, imageName);
        return imageFile;
    }

    public void deleteImageFile(int teamMemberID) {
        File imageFile = getImageFile(teamMemberID);
        imageFile.delete();
    }

    public void deleteImageFiles() {
        File[] files = mCacheDir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}
