package com.tab.whoiswho.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

public class TeamMemberListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<TeamMember> mTeamMembers;


    public TeamMemberListAdapter(Context context, List<TeamMember> teamMembers) {
        mInflater = LayoutInflater.from(context);
        mTeamMembers = teamMembers;
    }

    @Override
    public int getCount() {
        return mTeamMembers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeamMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TeamMemberViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.row_team_member_list, viewGroup, false);
            viewHolder = new TeamMemberViewHolder();
            viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
            viewHolder.txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
            viewHolder.imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
            view.setTag(viewHolder);
        } else {
            viewHolder = (TeamMemberViewHolder) view.getTag();
        }

        TeamMember teamMember = mTeamMembers.get(position);

        viewHolder.imgPhoto.setImageResource(R.drawable.photo_placeholder);
        viewHolder.imgPhoto.setTag(teamMember.getId());

        new DownloadImageTask(mInflater.getContext(), teamMember, viewHolder.imgPhoto).execute(teamMember.getImageURI());


        viewHolder.txtName.setText(teamMember.getName());
        viewHolder.txtJobTitle.setText(teamMember.getJobTitle());

        return view;
    }


    private static class TeamMemberViewHolder {
        public TextView txtName;
        public TextView txtJobTitle;
        public ImageView imgPhoto;
    }


}
