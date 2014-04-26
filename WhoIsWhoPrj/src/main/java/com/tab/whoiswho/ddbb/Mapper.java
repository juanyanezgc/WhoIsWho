package com.tab.whoiswho.ddbb;

import android.database.Cursor;

import com.tab.whoiswho.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static List<TeamMember> mapRestaurants(Cursor teamMembersCursor) {
        List<TeamMember> teamMembers = new ArrayList<TeamMember>();

        if (teamMembersCursor != null && teamMembersCursor.getCount() > 0) {
            while (teamMembersCursor.moveToNext()) {
                teamMembers.add(mapTeamMember(teamMembersCursor));
            }
        }

        return teamMembers;
    }

    public static TeamMember mapTeamMember(Cursor teamMemberCursor) {


        int id = teamMemberCursor.getInt(teamMemberCursor.getColumnIndex(DBManager.ID_COLUMN));
        String name = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.NAME_COLUMN));
        String jobTitle = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.JOB_TITLE_COLUMN));
        String biography = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.BIOGRAPHY_COLUMN));
        String imageURI = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.IMAGE_URI_COLUMN));

        TeamMember teamMember = new TeamMember(id, name, jobTitle, biography, imageURI);

        return teamMember;
    }


}
