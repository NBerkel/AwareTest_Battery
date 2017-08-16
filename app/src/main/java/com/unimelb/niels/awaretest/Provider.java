package com.unimelb.niels.awaretest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aware.Aware;
import com.aware.utils.DatabaseHelper;

import java.util.HashMap;

/**
 * Created by nielsv on 08-15-2017.
 */

public class Provider extends ContentProvider {

    public static final int DATABASE_VERSION = 1;
    public static String AUTHORITY = "com.unimelb.niels.awaretest.provider.survey";

    public static String DATABASE_NAME = "niels_awaretest.db";

    public static final String[] DATABASE_TABLES = {
            "niels_test"
    };

    public static final String[] TABLES_FIELDS = {
    };

    private static UriMatcher sUriMatcher = null;
    private static HashMap<String, String> surveyMap = null;
    private static HashMap<String, String> motivationMap = null;
    private DatabaseHelper dbHelper = null;
    private static SQLiteDatabase database = null;

    private void initialiseDatabase() {
        if (dbHelper == null)
            dbHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS);
        if (database == null)
            database = dbHelper.getWritableDatabase();
    }


    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getPackageName() + ".provider.survey";

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        initialiseDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
//        try {
//            Cursor c = qb.query(database, projection, selection, selectionArgs,
//                    null, null, sortOrder);
//            c.setNotificationUri(getContext().getContentResolver(), uri);
//            return c;
//        } catch (IllegalStateException e) {
//            if (Aware.DEBUG)
//                Log.e(Aware.TAG, e.getMessage());
//
//            return null;
//        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues initialValues) {
        initialiseDatabase();
        if (database == null) return null;

        ContentValues values = (initialValues != null) ? new ContentValues(initialValues) : new ContentValues();

        database.beginTransaction();

        switch (sUriMatcher.match(uri)) {
            default:
                database.endTransaction();
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        initialiseDatabase();
        if (database == null) return 0;

        database.beginTransaction();

        int count;
        switch (sUriMatcher.match(uri)) {
//            default:
//                database.endTransaction();
//                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        database.setTransactionSuccessful();
        database.endTransaction();

        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        initialiseDatabase();
        if (database == null) return 0;

        database.beginTransaction();

        int count;
        switch (sUriMatcher.match(uri)) {
//            default:
//                database.endTransaction();
//                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
        return 1;
    }
}
