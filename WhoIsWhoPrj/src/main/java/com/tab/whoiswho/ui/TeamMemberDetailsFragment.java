package com.tab.whoiswho.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tab.whoiswho.R;
import com.tab.whoiswho.logic.ImageLoader;
import com.tab.whoiswho.logic.WhoIsWhoApplication;
import com.tab.whoiswho.model.TeamMember;

public class TeamMemberDetailsFragment extends Fragment {

    public static TeamMemberDetailsFragment newInstance(TeamMember teamMember) {
        Bundle args = new Bundle();
        args.putParcelable(TEAM_MEMBER_KEY, teamMember);

        TeamMemberDetailsFragment teamMemberDetailsFragment = new TeamMemberDetailsFragment();
        teamMemberDetailsFragment.setArguments(args);
        return teamMemberDetailsFragment;
    }

    private static final String TEAM_MEMBER_KEY = "TeamMember";
    private TeamMember mTeamMember;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_member_details, container, false);


        if (savedInstanceState == null) {
            mTeamMember = getArguments().getParcelable(TEAM_MEMBER_KEY);
        } else {
            mTeamMember = savedInstanceState.getParcelable(TEAM_MEMBER_KEY);
        }


        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtJobTitle = (TextView) view.findViewById(R.id.txtJobTitle);
        TextView txtBiography = (TextView) view.findViewById(R.id.txtBiography);
        ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);

        txtName.setText(mTeamMember.getName());
        txtJobTitle.setText(mTeamMember.getJobTitle());
        txtBiography.setText(mTeamMember.getBiography());
        imgPhoto.setTag(mTeamMember.getId());
        ImageLoader imageLoader = WhoIsWhoApplication.getImageLoader();
        imageLoader.loadImage(mTeamMember, imgPhoto);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        ActionBarActivity hostActivity = (ActionBarActivity) getActivity();
        hostActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTeamMember = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(TEAM_MEMBER_KEY, mTeamMember);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setHomeButtonEnabled(false);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        return true;
    }
}
