package com.tab.whoiswho.parser;

import com.tab.whoiswho.model.TeamMember;
import com.tab.whoiswho.utils.Debug;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public static final String TEAM_MEMBERS_SECTION_KEY = "users";
    public static final String TEAM_MEMBER_KEY = "col";
    public static final String TEAM_MEMBER_IMAGE_URI_KEY = "src";

    public static List<TeamMember> parseTeamMembers(Document htmlDocument) {
        Debug.logInfo("Parsing team member html");

        List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        Element teamMembersSection = htmlDocument.body().getElementById(TEAM_MEMBERS_SECTION_KEY);
        Elements teamMembersHtml = teamMembersSection.getElementsByClass(TEAM_MEMBER_KEY);

        int id = 0;

        for (Element teamMemberHtml : teamMembersHtml) {

            Elements teamMemberElements = teamMemberHtml.getAllElements();

            String imageURI = teamMemberElements.get(2).attr(TEAM_MEMBER_IMAGE_URI_KEY);
            String name = teamMemberElements.get(3).text();
            String jobTitle = teamMemberElements.get(4).text();
            String biography = teamMemberElements.get(5).text();

            TeamMember teamMember = new TeamMember(id,name, jobTitle, biography, imageURI);
            teamMembers.add(teamMember);

            id++;
        }


        return teamMembers;
    }

}
