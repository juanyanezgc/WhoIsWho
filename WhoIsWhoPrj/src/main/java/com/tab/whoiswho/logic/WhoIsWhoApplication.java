package com.tab.whoiswho.logic;

import android.app.Application;

public class WhoIsWhoApplication extends Application {

    private static ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mImageLoader = new ImageLoader(getApplicationContext());
    }


    public static ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
