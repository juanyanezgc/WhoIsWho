package com.tab.whoiswho.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.logic.ImageLoader;
import com.tab.whoiswho.model.TeamMember;

public class TeamMemberDetailsFragment extends Fragment {

    private static final String TEAM_MEMBER_KEY = "TeamMember";

    public static TeamMemberDetailsFragment newInstance(TeamMember teamMember) {
        Bundle args = new Bundle();
        args.putParcelable(TEAM_MEMBER_KEY, teamMember);

        TeamMemberDetailsFragment teamMemberDetailsFragment = new TeamMemberDetailsFragment();
        teamMemberDetailsFragment.setArguments(args);
        return teamMemberDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_member_details, container, false);

        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
        TextView txtBiography = (TextView) view.findViewById(R.id.txtBiography);
        ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);


        TeamMember teamMember = getArguments().getParcelable(TEAM_MEMBER_KEY);

        txtName.setText(teamMember.getName());
        txtJobTitle.setText(teamMember.getJobTitle());
        txtBiography.setText(teamMember.getBiography());
        imgPhoto.setTag(teamMember.getId());
        ImageLoader imageLoader = new ImageLoader(getActivity());
        imageLoader.loadImage(teamMember, imgPhoto);


        return view;
    }


}
