package com.tab.whoiswho.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.tab.whoiswho.R;
import com.tab.whoiswho.model.TeamMember;


public class WhoIsWhoActivity extends ActionBarActivity implements TeamMembersListFragment.TeamMembersListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_is_who);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TeamMembersListFragment())
                    .commit();
        }

    }

    @Override
    public void onTeamMemberPressed(TeamMember teamMember) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


    }
}
