package com.tab.whoiswho.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tab.whoiswho.model.TeamMember;
import com.tab.whoiswho.parser.HtmlParser;
import com.tab.whoiswho.utils.Debug;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import static com.tab.whoiswho.utils.Constants.TEAM_MEMBER_LIST_URL;

public class TeamMembersListFragment extends ListFragment {

    public interface TeamMembersListFragmentListener {
        public void onTeamMemberPressed(TeamMember teamMember);
    }

    private TeamMembersListFragmentListener mListener;
    private ParseTeamMembersTask mParseTeamMembersTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParseTeamMembersTask = new ParseTeamMembersTask(this);
        mParseTeamMembersTask.execute();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TeamMembersListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TeamMembersListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        if (mParseTeamMembersTask.getStatus() == AsyncTask.Status.RUNNING) {
            mParseTeamMembersTask.cancel(true);
        }

        mParseTeamMembersTask = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onTeamMemberPressed((TeamMember) getListAdapter().getItem(position));
        }
    }

    private void fillListData(List<TeamMember> teamMembers) {
        ListAdapter adapter = new TeamMemberListAdapter(getActivity(), teamMembers);
        setListAdapter(adapter);
    }

    private static class ParseTeamMembersTask extends AsyncTask<Void, Void, List<TeamMember>> {

        private WeakReference<TeamMembersListFragment> mTeamMembersFragmentWeakReference;

        public ParseTeamMembersTask(TeamMembersListFragment teamMembersListFragment) {
            mTeamMembersFragmentWeakReference = new WeakReference<TeamMembersListFragment>(teamMembersListFragment);
        }


        @Override
        protected List<TeamMember> doInBackground(Void... params) {

            try {
                Debug.logInfo("Downloading team member html");
                Document document = Jsoup.connect(TEAM_MEMBER_LIST_URL).get();
                return HtmlParser.parseTeamMembers(document);
            } catch (IOException e) {
                Debug.logError(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TeamMember> teamMembers) {

            if (mTeamMembersFragmentWeakReference != null && teamMembers != null) {
                mTeamMembersFragmentWeakReference.get().fillListData(teamMembers);
            }

        }
    }

}
