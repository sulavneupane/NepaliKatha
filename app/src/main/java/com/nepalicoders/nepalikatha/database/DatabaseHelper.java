package com.nepalicoders.nepalikatha.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kayub on 7/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NepaliKatha";

    public static final String DATABASE_TABLE_POEMS = "tbl_poems";
    public static final String KEY_ITEM_ID = "_id";
    public static final String KEY_ITEM_TITLE = "_title";
    public static final String KEY_ITEM_WRITER = "_writer";
    public static final String KEY_ITEM_CONTENT = "_content";
    public static final String KEY_ITEM_CATEGORY = "_category";
    public static final String KEY_ITEM_PUBLISHED_ON = "_published_on";
    public static final String KEY_ITEM_SUBMITTED_BY = "_submitted_by";
    public static final String KEY_ITEM_EMAIL = "_email";

    public static String CREATE_TABLE_POEMS = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_POEMS +
            "(" +
            KEY_ITEM_ID + " INT PRIMARY KEY NOT NULL, " +
            KEY_ITEM_TITLE + " TEXT NOT NULL, " +
            KEY_ITEM_WRITER + " TEXT NOT NULL, " +
            KEY_ITEM_CONTENT + " TEXT NOT NULL, " +
            KEY_ITEM_CATEGORY + " TEXT NOT NULL, " +
            KEY_ITEM_PUBLISHED_ON + " TEXT NOT NULL, " +
            KEY_ITEM_SUBMITTED_BY + " TEXT NOT NULL, " +
            KEY_ITEM_EMAIL + " TEXT NOT NULL)";


    public static final String DATABASE_TABLE_FAVOURITES = "tbl_favourites";
    public static final String KEY_FAVOURITES_ITEM_ID = "_favourites_id";
    public static final String KEY_FAVOURITES_ITEM_TITLE = "_favourites_title";
    public static final String KEY_FAVOURITES_ITEM_WRITER = "_favourites_writer";
    public static final String KEY_FAVOURITES_ITEM_CONTENT = "_favourites_content";
    public static final String KEY_FAVOURITES_ITEM_CATEGORY = "_favourites_category";
    public static final String KEY_FAVOURITES_ITEM_PUBLISHED_ON = "_favourites_published_on";
    public static final String KEY_FAVOURITES_ITEM_SUBMITTED_BY = "_favourites_submitted_by";
    public static final String KEY_FAVOURITES_ITEM_EMAIL = "_favourites_email";

    public static String CREATE_TABLE_FAVOURITES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_FAVOURITES +
            "(" +
            KEY_FAVOURITES_ITEM_ID + " INT PRIMARY KEY NOT NULL, " +
            KEY_FAVOURITES_ITEM_TITLE + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_WRITER + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_CONTENT + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_CATEGORY + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_PUBLISHED_ON + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_SUBMITTED_BY + " TEXT NOT NULL, " +
            KEY_FAVOURITES_ITEM_EMAIL + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POEMS);
        db.execSQL(CREATE_TABLE_FAVOURITES);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + CREATE_TABLE_POEMS);
        db.execSQL("DROP TABLE IF EXIST " + CREATE_TABLE_FAVOURITES);
        onCreate(db);
    }
}