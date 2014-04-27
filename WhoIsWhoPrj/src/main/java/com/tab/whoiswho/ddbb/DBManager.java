package com.tab.whoiswho.ddbb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.tab.whoiswho.model.TeamMember;

import java.util.List;

public class DBManager {

    private static final String DATABASE_NAME = "WhoIsWho.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TEAM_MEMBER_TABLE_NAME = "TeamMember";

    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String JOB_TITLE_COLUMN = "jobTitle";
    public static final String BIOGRAPHY_COLUMN = "biography";
    public static final String IMAGE_URI_COLUMN = "imageUri";

	/* ============ SQL CREATE SENTENCES ============ */

    private static final String SQL_CREATE_TEAM_MEMBER_TABLE = "create table "
            + TEAM_MEMBER_TABLE_NAME + " (" + ID_COLUMN + " numeric not null,"
            + NAME_COLUMN + " text not null," + JOB_TITLE_COLUMN
            + " text not null," + BIOGRAPHY_COLUMN + " numeric not null,"
            + IMAGE_URI_COLUMN + " numeric not null, PRIMARY KEY(" + ID_COLUMN + "))";

	/* ============ SQL QUERY SENTENCES ============ */

    private static final String SQL_SELECT_ALL = "select * from "
            + TEAM_MEMBER_TABLE_NAME + ";";

    private static final String SQL_REMOVE_ALL = "delete from " + TEAM_MEMBER_TABLE_NAME;

	/* ============ SQL DROP SENTENCES ============ */

    private static final String SQL_DROP_RESTAURANTS = "DROP TABLE IF EXISTS "
            + TEAM_MEMBER_TABLE_NAME;

    private DataBaseHelper mDbHelper;

    public DBManager(Context context) {
        mDbHelper = new DataBaseHelper(context);
    }

    public void saveTeamMembers(List<TeamMember> teamMembers) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();

        db.execSQL(SQL_REMOVE_ALL);

        for (TeamMember teamMember : teamMembers) {
            ContentValues teamMemberValues = new ContentValues();
            teamMemberValues.put(ID_COLUMN, teamMember.getId());
            teamMemberValues.put(NAME_COLUMN, teamMember.getName());
            teamMemberValues.put(JOB_TITLE_COLUMN, teamMember.getJobTitle());
            teamMemberValues.put(BIOGRAPHY_COLUMN, teamMember.getBiography());
            teamMemberValues.put(IMAGE_URI_COLUMN, teamMember.getImageURI());

            db.insert(TEAM_MEMBER_TABLE_NAME, null, teamMemberValues);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public void updateTeamMemberImageURI(TeamMember teamMember){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(IMAGE_URI_COLUMN, teamMember.getImageURI());

        db.update(TEAM_MEMBER_TABLE_NAME, args, ID_COLUMN + "=" + "'" + teamMember.getId()
                + "'", null);

        db.close();
    }

    public List<TeamMember> getTeamMembers() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor restaurantsCursor = db.rawQuery(SQL_SELECT_ALL, null);
        return Mapper.mapRestaurants(restaurantsCursor);
    }


    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TEAM_MEMBER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP_RESTAURANTS);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion,
                                int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }
}
