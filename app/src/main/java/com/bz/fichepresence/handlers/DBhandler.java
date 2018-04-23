package com.bz.fichepresence.handlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rog on 11/04/2018.
 */

public class DBhandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "attendance.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PERSON = "person";

    public static final String  COLUMN_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_MATRICULE = "prenom";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE = "img";
    public static final String COLUMN_PRESENCE = "presence";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PERSON + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NOM + " TEXT, " +
                    COLUMN_MATRICULE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_PRESENCE + " INTEGER, " +
                    COLUMN_DATE + " TEXT );";

    public DBhandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_PERSON);
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }
}