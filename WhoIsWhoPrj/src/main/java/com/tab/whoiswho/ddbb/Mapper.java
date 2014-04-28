package com.tab.whoiswho.ddbb;

import android.database.Cursor;

import com.tab.whoiswho.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps database data to application model
 */
public class Mapper {

    /**
     * Maps database data for team members
     *
     * @param teamMembersCursor Cursor containing the data of all team members retrieved from the database
     * @return List with all the team members in the database
     */
    public static List<TeamMember> mapTeamMembers(Cursor teamMembersCursor) {
        List<TeamMember> teamMembers = new ArrayList<TeamMember>();

        if (teamMembersCursor != null && teamMembersCursor.getCount() > 0) {
            while (teamMembersCursor.moveToNext()) {
                teamMembers.add(mapTeamMember(teamMembersCursor));
            }
        }

        return teamMembers;
    }

    /**
     * Maps database data for a team member
     *
     * @param teamMemberCursor Cursor containing the data of a team member retrieved from the database
     * @return Team member
     */
    public static TeamMember mapTeamMember(Cursor teamMemberCursor) {


        int id = teamMemberCursor.getInt(teamMemberCursor.getColumnIndex(DBManager.ID_COLUMN));
        String name = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.NAME_COLUMN));
        String jobTitle = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.JOB_TITLE_COLUMN));
        String biography = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.BIOGRAPHY_COLUMN));
        String imageURI = teamMemberCursor.getString(teamMemberCursor.getColumnIndex(DBManager.IMAGE_URI_COLUMN));

        return new TeamMember(id, name, jobTitle, biography, imageURI);

    }


}
