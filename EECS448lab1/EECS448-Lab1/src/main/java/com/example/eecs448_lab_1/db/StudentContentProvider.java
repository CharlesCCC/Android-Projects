package com.example.eecs448_lab_1.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class StudentContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.eecs448_lab_1";
    private static final String BASE_PATH = "student";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int STUDENTS = 1;
    private static final int STUDENT = 2;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, STUDENTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", STUDENT);
    }

    SQLiteDatabase db;

    public StudentContentProvider() {

    }


    @Override
    public boolean onCreate() {
        db = new StudentDatabase(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                delCount = db.delete(StudentDatabase.TABLE, selection, selectionArgs);
                break;
            case STUDENT:
                delCount = db.delete(StudentDatabase.TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("insert uri: ", uri.toString());
        long id = db.insert(StudentDatabase.TABLE, null, values);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Log.i("query uri: ", uri.toString());
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case 1:
                cursor = db.query(StudentDatabase.TABLE, StudentDatabase.ALL_COLUMNS, selection
                        , null, null, null, StudentDatabase.NAME + " ASC");
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                updCount = db.update(StudentDatabase.TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
