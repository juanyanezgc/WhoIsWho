package com.tab.whoiswho.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.ddbb.DBManager;
import com.tab.whoiswho.model.TeamMember;
import com.tab.whoiswho.utils.Debug;
import com.tab.whoiswho.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the load of team members images
 */
public class ImageLoader {

    private static final int MAX_THREADS = 5;

    private ImagesCache mImagesCache;
    private FileManager mFileManager;
    private ExecutorService mThreadPool;

    public ImageLoader(Context context) {
        mImagesCache = new ImagesCache();
        mFileManager = new FileManager(context);
        mThreadPool = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * Loads a team member image either from the cache or from the web
     *
     * @param teamMember Team member
     * @param imageView  ImageView in which load the image to
     */
    public void loadImage(TeamMember teamMember, ImageView imageView) {
        if (teamMember == null || imageView == null) {
            throw new IllegalArgumentException("teamMember and imageView cannot be null");
        }

        Debug.logDebug("Loading image for team member " + teamMember.getId());

        Bitmap bitmap = mImagesCache.getCachedImage(teamMember.getId());

        if (bitmap != null) {
            Debug.logDebug("Image loaded from cache memory for team member " + teamMember.getId());
            imageView.setImageBitmap(bitmap);
        } else {
            ImageLoaderRunnable imageLoaderRunnable = new ImageLoaderRunnable(teamMember, imageView);
            mThreadPool.submit(imageLoaderRunnable);
            imageView.setImageResource(R.drawable.photo_placeholder);
        }


    }

    /**
     * Clears the cache
     */
    public void clearCache() {
        mImagesCache.clearCache();
        mFileManager.deleteImageFiles();
    }

    /**
     * Checks if the ImageView in which load the image to has been reused fo other team member by the adapter
     *
     * @param teamMemberID Identifier of the image
     * @param imageView    ImageView in which load the image to
     */
    private boolean isImageViewReused(int teamMemberID, ImageView imageView) {
        int tag = (Integer) imageView.getTag();
        return tag != teamMemberID;
    }


    private class ImageLoaderRunnable implements Runnable {

        private TeamMember mTeamMember;
        private ImageView mImageView;

        public ImageLoaderRunnable(TeamMember teamMember, ImageView imageView) {
            mTeamMember = teamMember;
            mImageView = imageView;
        }

        @Override
        public void run() {

            if (isImageViewReused(mTeamMember.getId(), mImageView)) {
                return;
            }

            Bitmap bitmap = loadBitmap();
            mImagesCache.putCachedImage(mTeamMember.getId(), bitmap);

            if (isImageViewReused(mTeamMember.getId(), mImageView)) {
                return;
            }

            mImageView.post(new BitmapLoader(bitmap, mImageView));

        }

        /**
         * Loads a team member image either from web or from phone storage*
         */
        private Bitmap loadBitmap() {

            File imageFile = mFileManager.getImageFile(mTeamMember.getId());

            Bitmap bitmap;

            if (imageFile.exists()) {
                bitmap = decodeFile(imageFile);
                Debug.logDebug("Image loaded from cache folder: " + mTeamMember.getImageURI());
                return bitmap;
            }

            try {
                Context context = mImageView.getContext();
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Debug.logDebug("Loading image from url: " + mTeamMember.getImageURI());
                    URL url = new URL(mTeamMember.getImageURI());
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();

                    Debug.logDebug("Image downloaded");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    Utils.copyStream(inputStream, outputStream);
                    inputStream.close();
                    outputStream.close();

                    Debug.logDebug("Saving image to cache folder: " + imageFile.getPath());
                    mTeamMember.setImageURI(imageFile.getPath());
                    DBManager dbManager = new DBManager(context);
                    dbManager.updateTeamMemberImageURI(mTeamMember);
                    bitmap = decodeFile(imageFile);

                    return bitmap;
                }

            } catch (MalformedURLException e) {
                Debug.logError(e.getMessage());
            } catch (FileNotFoundException e) {
                Debug.logError(e.getMessage());
            } catch (IOException e) {
                Debug.logError(e.getMessage());
            }

            return null;
        }

        /**
         * Decodes a bitmap
         *
         * @param imageFile File containing the image data
         * @return Bitmap of the image
         */
        private Bitmap decodeFile(File imageFile) {

            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(imageFile), null, opt);
                int pixels = mImageView.getContext().getResources().getDimensionPixelSize(R.dimen.photo_size);
                opt.inSampleSize = Utils.calculateInSampleSize(opt,
                        pixels, pixels);
                opt.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(new FileInputStream(imageFile), null, opt);
            } catch (FileNotFoundException e) {
                Debug.logError(e.getMessage());
            }

            return null;
        }


        /**
         * Loads a bitmap into the team member ImageView
         */
        private class BitmapLoader implements Runnable {
            private Bitmap mImage;
            private ImageView mImageView;


            public BitmapLoader(Bitmap image, ImageView imageView) {
                mImage = image;
                mImageView = imageView;
            }

            @Override
            public void run() {
                if (isImageViewReused(mTeamMember.getId(), mImageView)) {
                    return;
                }

                if (mImage != null) {
                    Animation fadeIn = AnimationUtils.loadAnimation(mImageView.getContext(), R.anim.fade_in_image);
                    mImageView.setImageBitmap(mImage);
                    mImageView.setAnimation(fadeIn);
                    fadeIn.start();
                } else {
                    mImageView.setImageResource(R.drawable.photo_placeholder);
                }
            }
        }

    }


}
