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
    public static final String TEAM_MEMBER_NAME_KEY = "h3";
    public static final String TEAM_MEMBER_PARAGRAPH_KEY = "p";

    public static List<TeamMember> parseTeamMembers(Document htmlDocument) {
        Debug.logInfo("Parsing team member html");

        List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        Element teamMembersSection = htmlDocument.body().getElementById(TEAM_MEMBERS_SECTION_KEY);
        Elements teamMembersHtml = teamMembersSection.getElementsByClass(TEAM_MEMBER_KEY);

        for (Element teamMemberHtml : teamMembersHtml) {

            String imageURI = teamMemberHtml.getElementsByAttribute(TEAM_MEMBER_IMAGE_URI_KEY).attr(TEAM_MEMBER_IMAGE_URI_KEY);
            String name = teamMemberHtml.getElementsByTag(TEAM_MEMBER_NAME_KEY).text();

            Elements paragraphElements =  teamMemberHtml.getElementsByTag(TEAM_MEMBER_PARAGRAPH_KEY);
            String jobTitle = paragraphElements.get(0).text();
            String biography = paragraphElements.get(1).text();

            TeamMember teamMember = new TeamMember(name, jobTitle, biography, imageURI);
            teamMembers.add(teamMember);

        }


        return teamMembers;
    }

}