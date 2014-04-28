package com.tab.whoiswho.logic;

import android.graphics.Bitmap;

import com.tab.whoiswho.utils.Utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImagesCache {

    private Map<Integer, Bitmap> mCachedImages;
    private long mAllocatedMemory;
    private long mMemoryLimit;


    public ImagesCache() {
        mCachedImages = new LinkedHashMap<Integer, Bitmap>();
        //use 25% of available heap size
        mMemoryLimit = Runtime.getRuntime().maxMemory() / 4;

    }

    public Bitmap getCachedImage(int teamMemberID) {
        return mCachedImages.containsKey(teamMemberID) ? mCachedImages.get(teamMemberID) : null;
    }

    public void putCachedImage(int teamMemberID, Bitmap cachedImage) {


        if (mCachedImages.containsKey(teamMemberID)) {
            mAllocatedMemory -= Utils.getBitmapBytesSize(mCachedImages.get(teamMemberID));
        }

        mCachedImages.put(teamMemberID, cachedImage);
        mAllocatedMemory += Utils.getBitmapBytesSize(cachedImage);

        checkAvailableMemory();

    }

    public void clearCache() {
        mCachedImages.clear();
        mAllocatedMemory = 0;
    }

    private void checkAvailableMemory() {
        if (mAllocatedMemory > mMemoryLimit) {

            Iterator<Map.Entry<Integer, Bitmap>> iterator = mCachedImages.entrySet().iterator();

            while (iterator.hasNext() && mAllocatedMemory > mMemoryLimit) {
                Map.Entry<Integer, Bitmap> entry = iterator.next();
                mAllocatedMemory -= Utils.getBitmapBytesSize(entry.getValue());
                iterator.remove();
            }

        }
    }


}
