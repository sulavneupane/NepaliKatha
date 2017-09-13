package com.nepalicoders.nepalikatha.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepalikatha.application.App;
import com.nepalicoders.nepalikatha.objects.Poem;

import java.sql.SQLException;

/**
 * Created by Kayub on 7/26/2015.
 */
public class DatabaseHandler {

    private DatabaseHelper ourHelper;
    private Context context;
    private SQLiteDatabase ourDatabase;


    Tracker mTracker;

    int i = 0;

    public DatabaseHandler(Context c) {
        context = c;
        mTracker = ((App) (((Activity) c).getApplication())).getDefaultTracker();
        mTracker.setScreenName("Database Operations");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public DatabaseHandler Open() throws SQLException {
        ourHelper = new DatabaseHelper(context);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public Long addPoemToDatabase(Poem poem) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.KEY_ITEM_ID, poem.getId());
        cv.put(DatabaseHelper.KEY_ITEM_TITLE, poem.getTitle());
        cv.put(DatabaseHelper.KEY_ITEM_WRITER, poem.getWriter());
        cv.put(DatabaseHelper.KEY_ITEM_CONTENT, poem.getContent());
        cv.put(DatabaseHelper.KEY_ITEM_CATEGORY, poem.getCategory());
        cv.put(DatabaseHelper.KEY_ITEM_PUBLISHED_ON, poem.getPublishedOn());
        cv.put(DatabaseHelper.KEY_ITEM_SUBMITTED_BY, poem.getSubmittedBy());
        cv.put(DatabaseHelper.KEY_ITEM_EMAIL, poem.getEmail());

        /*mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AddPoemToDatabase")
                .setAction("Poem = " + poem.getTitle() + " Writer = " + poem.getWriter())
                .build());*/

        return ourDatabase.insert(DatabaseHelper.DATABASE_TABLE_POEMS, null, cv);
    }

    public Cursor getAllPoemsFromDatabase() {
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_POEMS, null, null, null, null, null, DatabaseHelper.KEY_ITEM_ID + " DESC");
    }

    public Cursor getAllNepaliPoemsFromDatabase() {
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_POEMS, null, DatabaseHelper.KEY_ITEM_CATEGORY+" = ?", new String[] { "Nepali" }, null, null, DatabaseHelper.KEY_ITEM_ID+" DESC");
    }

    public Cursor getAllEnglishPoemsFromDatabase() {
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_POEMS, null, DatabaseHelper.KEY_ITEM_CATEGORY+" = ?", new String[] { "English" }, null, null, DatabaseHelper.KEY_ITEM_ID+" DESC");
    }

    public int removePoemFromDatabase(long id) {
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_POEMS, DatabaseHelper.KEY_ITEM_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int removeAllPoemsFromDatabase() {
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_POEMS, null, null);
    }

    public Long addPoemToFavourite(Poem poem) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_ID, poem.getId());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_TITLE, poem.getTitle());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_WRITER, poem.getWriter());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_CONTENT, poem.getContent());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_CATEGORY, poem.getCategory());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_PUBLISHED_ON, poem.getPublishedOn());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_SUBMITTED_BY, poem.getSubmittedBy());
        cv.put(DatabaseHelper.KEY_FAVOURITES_ITEM_EMAIL, poem.getEmail());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AddStoryToFavourite")
                .setAction("Story = " + poem.getTitle() + " Writer = " + poem.getWriter())
                .build());

        return ourDatabase.insert(DatabaseHelper.DATABASE_TABLE_FAVOURITES, null, cv);
    }

    public Cursor getAllPoemsFromFavourite() {
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_FAVOURITES, null, null, null, null, null, null);
    }

    public int removePoemFromFavourite(long id) {
        return ourDatabase.delete(DatabaseHelper.DATABASE_TABLE_FAVOURITES, DatabaseHelper.KEY_FAVOURITES_ITEM_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getFavouritePoem(String id){
        return ourDatabase.query(DatabaseHelper.DATABASE_TABLE_FAVOURITES, null, DatabaseHelper.KEY_FAVOURITES_ITEM_ID + "=?", new String[]{id}, null, null, null);
    }

}