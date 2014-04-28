package com.tab.whoiswho.logic;

import android.graphics.Bitmap;

import com.tab.whoiswho.utils.Utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implements a cache for team members images
 */
public class ImagesCache {

    /**
     * Cached images
     */
    private Map<Integer, Bitmap> mCachedImages;
    /**
     * Current memory used by cached images
     */
    private long mAllocatedMemory;
    /**
     * Max memory allowed for cached images
     */
    private long mMemoryLimit;


    public ImagesCache() {
        mCachedImages = new LinkedHashMap<Integer, Bitmap>();
        mMemoryLimit = Runtime.getRuntime().maxMemory() / 4;

    }

    /**
     * Gets a cached image if exists
     *
     * @param teamMemberID Identifier of the image
     * @return Bitmap with the cached image
     */
    public Bitmap getCachedImage(int teamMemberID) {
        return mCachedImages.containsKey(teamMemberID) ? mCachedImages.get(teamMemberID) : null;
    }

    /**
     * Stores a new image in the cache
     *
     * @param teamMemberID Identifier of the image
     * @param cachedImage  Bitmap with the cached image
     */
    public void putCachedImage(int teamMemberID, Bitmap cachedImage) {


        if (mCachedImages.containsKey(teamMemberID)) {
            mAllocatedMemory -= Utils.getBitmapBytesSize(mCachedImages.get(teamMemberID));
        }

        mCachedImages.put(teamMemberID, cachedImage);
        mAllocatedMemory += Utils.getBitmapBytesSize(cachedImage);

        checkAvailableMemory();

    }

    /**
     * Clears the cache
     */
    public void clearCache() {
        mCachedImages.clear();
        mAllocatedMemory = 0;
    }

    /**
     * Checks if there is enough memory available for a new image.
     * In case there isn't memory available, removes the least recently accessed image
     */
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
