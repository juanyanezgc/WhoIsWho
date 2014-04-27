package com.tab.whoiswho.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.ddbb.DBManager;
import com.tab.whoiswho.model.TeamMember;
import com.tab.whoiswho.utils.Debug;
import com.tab.whoiswho.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> mPhotoImageViewWeakReference;
    private Context mContext;
    private TeamMember mTeamMember;


    public DownloadImageTask(Context context, TeamMember teamMember, ImageView photoImageView) {
        mContext = context;
        mTeamMember = teamMember;
        mPhotoImageViewWeakReference = new WeakReference<ImageView>(photoImageView);
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        try {

            if (mTeamMember.getImageURI().startsWith("http")) {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                String filePath = mContext.getFilesDir().getAbsolutePath() + File.separator + mTeamMember.getId() + ".jpg";
                FileOutputStream outputStream = new FileOutputStream(filePath);
                Utils.copyStream(inputStream, outputStream);

                mTeamMember.setImageURI(filePath);
                DBManager dbManager = new DBManager(mContext);
                dbManager.updateTeamMemberImageURI(mTeamMember);

            }


            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(mTeamMember.getImageURI(), opt);


            int pixels = mContext.getResources().getDimensionPixelSize(R.dimen.photo_size);

            opt.inSampleSize = Utils.calculateInSampleSize(opt,
                    pixels, pixels);

            opt.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(mTeamMember.getImageURI(), opt);


        } catch (IOException e) {
            Debug.logError(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = mPhotoImageViewWeakReference.get();
        if (imageView != null && bitmap != null) {
            int id = (Integer) imageView.getTag();

            if (id == mTeamMember.getId()) {

                mPhotoImageViewWeakReference.get().setImageBitmap(bitmap);
            }
        }
    }


}
