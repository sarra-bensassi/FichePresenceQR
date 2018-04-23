package com.bz.fichepresence.dbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bz.fichepresence.entities.Personne;
import com.bz.fichepresence.handlers.DBhandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rog on 11/04/2018.
 */

public class PersonOps {
    public static final String LOGTAG = "PERSON_MNGMNT_SYS";

        SQLiteOpenHelper dbhandler;
        SQLiteDatabase database;

        private static final String[] allColumns = {
                DBhandler.COLUMN_ID ,
                DBhandler.COLUMN_NOM,
                DBhandler.COLUMN_MATRICULE,
                DBhandler.COLUMN_IMAGE,
                DBhandler.COLUMN_PRESENCE,
                DBhandler.COLUMN_DATE
        };

        public Personne addpref (Personne p){
            ContentValues values  = new ContentValues();
            values.put(DBhandler.COLUMN_NOM,p.getNom());

            values.put(DBhandler.COLUMN_IMAGE,p.getImg());
            values.put(DBhandler.COLUMN_MATRICULE,p.getMatricule());
            values.put(DBhandler.COLUMN_PRESENCE,p.getPresent());
            values.put(DBhandler.COLUMN_DATE,p.getArrive());
            int insertid = (int) database.insert(DBhandler.TABLE_PERSON,null,values);
            p.setId(insertid);
            return p;
        }
    public void  updatePersonne(Personne p ){
        ContentValues newValue = new ContentValues();
        newValue.put(DBhandler.COLUMN_PRESENCE,p.getPresent());
        newValue.put(DBhandler.COLUMN_DATE,p.getArrive());
        database.update(DBhandler.TABLE_PERSON,newValue,
                DBhandler.COLUMN_MATRICULE + "=?", new String[]{String.valueOf(p.getMatricule())});
    }
    public List<Personne> getAll() {

        Cursor cursor = database.query(DBhandler.TABLE_PERSON, allColumns, null, null, null, null, null);

        List<Personne> ps = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Personne p = new Personne();
                p.setId(cursor.getInt(cursor.getColumnIndex(DBhandler.COLUMN_ID)));
                p.setNom(cursor.getString(cursor.getColumnIndex(DBhandler.COLUMN_NOM)));
                p.setPresent(cursor.getInt(cursor.getColumnIndex(DBhandler.COLUMN_PRESENCE)));
                p.setArrive(cursor.getString(cursor.getColumnIndex(DBhandler.COLUMN_DATE)));
                p.setImg(cursor.getString(cursor.getColumnIndex(DBhandler.COLUMN_IMAGE)));
                p.setMatricule(cursor.getString(cursor.getColumnIndex(DBhandler.COLUMN_MATRICULE)));
                ps.add(p);
            }
        }
        return ps;
    }
        public PersonOps (Context context){
            dbhandler = new DBhandler(context);
        }

        public void open(){
            Log.i(LOGTAG,"Database Opened");
            database = dbhandler.getWritableDatabase();
        }
        public void close() {
            Log.i(LOGTAG, "Database Closed");
            dbhandler.close();

        }  }