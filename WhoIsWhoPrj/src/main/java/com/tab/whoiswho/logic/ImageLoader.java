package com.tab.whoiswho.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.ddbb.DBManager;
import com.tab.whoiswho.model.TeamMember;
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

        Bitmap bitmap = mImagesCache.getCachedImage(teamMember.getId());

        if (bitmap != null) {
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
            Bitmap bitmap = decodeFile(imageFile);

            if (bitmap != null) {
                return bitmap;
            }

            try {
                Context context = mImageView.getContext();

                URL url = new URL(mTeamMember.getImageURI());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                Utils.copyStream(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
                bitmap = decodeFile(imageFile);
                DBManager dbManager = new DBManager(context);
                dbManager.updateTeamMemberImageURI(mTeamMember);

                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
