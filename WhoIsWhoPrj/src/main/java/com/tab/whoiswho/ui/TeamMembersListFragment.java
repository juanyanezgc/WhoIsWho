package com.tab.whoiswho.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tab.whoiswho.R;
import com.tab.whoiswho.ddbb.DBManager;
import com.tab.whoiswho.logic.ImageLoader;
import com.tab.whoiswho.logic.WhoIsWhoApplication;
import com.tab.whoiswho.model.TeamMember;
import com.tab.whoiswho.parser.HtmlParser;
import com.tab.whoiswho.utils.Debug;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tab.whoiswho.utils.Constants.TEAM_MEMBER_LIST_URL;

public class TeamMembersListFragment extends ListFragment {

    private static final String TEAM_MEMBERS_KEY = "teamMembers";
    private static final int HTML_ERROR = 1;
    private static final int NETWORK_ERROR = 2;

    private List<TeamMember> mTeamMembers;


    public interface TeamMembersListFragmentListener {
        public void onTeamMemberPressed(TeamMember teamMember);
    }

    private TeamMembersListFragmentListener mListener;
    private ParseTeamMembersTask mParseTeamMembersTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            if (mTeamMembers == null) {
                downloadTeamMembers();
            }
        } else {
            mTeamMembers = savedInstanceState.getParcelableArrayList(TEAM_MEMBERS_KEY);
            if (mTeamMembers == null) {
                downloadTeamMembers();
            } else {
                fillListData();
            }
        }
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

        if (mParseTeamMembersTask != null && mParseTeamMembersTask.getStatus() == AsyncTask.Status.RUNNING) {
            mParseTeamMembersTask.cancel(true);
        }

        mParseTeamMembersTask = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_reload, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mParseTeamMembersTask != null && mParseTeamMembersTask.getStatus() == AsyncTask.Status.RUNNING) {
            mParseTeamMembersTask.cancel(true);
        }

        setListShown(false);
        downloadTeamMembers();

        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onTeamMemberPressed((TeamMember) getListAdapter().getItem(position));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TEAM_MEMBERS_KEY, (ArrayList<TeamMember>) mTeamMembers);
    }

    private void downloadTeamMembers() {
        mParseTeamMembersTask = new ParseTeamMembersTask();
        mParseTeamMembersTask.execute();
    }

    private void fillListData() {
        ListAdapter adapter = new TeamMemberListAdapter(getActivity(), mTeamMembers);
        setListAdapter(adapter);
        setListShown(true);
    }

    private Handler mErrorHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HTML_ERROR:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.parsing_error, Toast.LENGTH_LONG).show();
                    }
                    break;
                case NETWORK_ERROR:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    private class ParseTeamMembersTask extends AsyncTask<Void, Void, List<TeamMember>> {

        @Override
        protected List<TeamMember> doInBackground(Void... params) {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            DBManager dbManager = new DBManager(getActivity());

            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    Debug.logInfo("Downloading team member html from: " + TEAM_MEMBER_LIST_URL);
                    Document document = Jsoup.connect(TEAM_MEMBER_LIST_URL).get();
                    List<TeamMember> teamMembers = HtmlParser.parseTeamMembers(document);
                    dbManager.saveTeamMembers(teamMembers);
                    ImageLoader imageLoader = WhoIsWhoApplication.getImageLoader();
                    imageLoader.clearCache();
                    return teamMembers;

                } catch (IOException e) {
                    Debug.logError(e.getMessage());
                    mErrorHandler.sendEmptyMessage(HTML_ERROR);
                    return dbManager.getTeamMembers();
                }
            } else {
                mErrorHandler.sendEmptyMessage(NETWORK_ERROR);
                return dbManager.getTeamMembers();
            }

        }

        @Override
        protected void onPostExecute(List<TeamMember> teamMembers) {

            if (getActivity() != null) {
                mTeamMembers = teamMembers;
                fillListData();
            }

        }
    }

}
