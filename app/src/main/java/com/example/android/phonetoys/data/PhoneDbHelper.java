package com.example.android.phonetoys.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

/**
 * Database helper for Phones app. Manages database creation and version management.
 */
public class PhoneDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PhoneDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "phonetracker.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link PhoneDbHelper}.
     *
     * @param context of the app
     */
    public PhoneDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the phones table
        String SQL_CREATE_PHONES_TABLE =  "CREATE TABLE " + PhoneEntry.TABLE_NAME + " ("
                + PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhoneEntry.COLUMN_PHONE_NAME + " TEXT NOT NULL, "
                + PhoneEntry.COLUMN_PHONE_QUANTITY + " INTEGER NOT NULL, "
                + PhoneEntry.COLUMN_PHONE_PRICE + " INTEGER NOT NULL, "
                + PhoneEntry.COLUMN_CONTACT_INFO + " TEXT NOT NULL, "
                + PhoneEntry.COLUMN_PHONE_PICTURE + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PHONES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}