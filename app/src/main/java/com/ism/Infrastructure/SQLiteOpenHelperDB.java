package com.ism.Infrastructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteOpenHelperDB extends SQLiteOpenHelper {

    public final static int WERSJA_BAZY = 2;
    public final static String ID = "_id";
    public final static String NAZWA_BAZY = "secondApp.db";
    public final static String NAZWA_TABELI = "websites";
    public final static String KOLUMNA1 = "name";
    public final static String KOLUMNA2 = "description";
    public final static String KOLUMNA3 = "url";
    private static final String KAS_BAZY = "DROP TABLE IF EXISTS "+NAZWA_TABELI;
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI +
            "("+ID+" integer primary key autoincrement, " +
            KOLUMNA1+" TEXT NOT NULL,"+
            KOLUMNA2+" TEXT NOT NULL,"+
            KOLUMNA3+" TEXT NOT NULL);";


    public SQLiteOpenHelperDB(@Nullable Context context) {
        // constructor - create helper
        super(context, NAZWA_BAZY, null, WERSJA_BAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TW_BAZY); // execute create sql query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KAS_BAZY); // execute delete table sql query
        onCreate(db); // create table
    }
}
