package com.tab.whoiswho.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.model.TeamMember;

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
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TeamMemberViewHolder viewHolder;

        if(view == null){
            view = mInflater.inflate(R.layout.row_team_member_list,viewGroup,false);
            viewHolder = new TeamMemberViewHolder();
            viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
            viewHolder.txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
            viewHolder.imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
            view.setTag(viewHolder);
        }else{
            viewHolder = (TeamMemberViewHolder) view.getTag();
        }

        TeamMember teamMember = mTeamMembers.get(position);

        viewHolder.txtName.setText(teamMember.getName());
        viewHolder.txtJobTitle.setText(teamMember.getJobTitle());

        return view;
    }


    private static class TeamMemberViewHolder{
        public TextView txtName;
        public TextView txtJobTitle;
        public ImageView imgPhoto;
    }

    private static class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

        }



    }
}
