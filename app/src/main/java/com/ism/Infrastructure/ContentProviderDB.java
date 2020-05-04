package com.ism.Infrastructure;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ContentProviderDB extends ContentProvider {

    private SQLiteOpenHelperDB sqLiteOpenHelperDB;
    //identyfikator (ang. authority) dostawcy
    private static final String IDENTYFIKATOR = "com.example.baza_danych.MojProvider";
    //stała – aby nie trzeba było wpisywać tekstu samodzielnie
    public static final Uri URI_ZAWARTOSCI = Uri.parse("content://" + IDENTYFIKATOR + "/" + SQLiteOpenHelperDB.NAZWA_TABELI);
    //stałe pozwalające zidentyfikować rodzaj rozpoznanego URI
    private static final int CALA_TABELA = 1;
    private static final int WYBRANY_WIERSZ = 2;
    //UriMacher z pustym korzeniem drzewa URI (NO_MATCH)
    private static final UriMatcher sDopasowanieUri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sDopasowanieUri.addURI(IDENTYFIKATOR, SQLiteOpenHelperDB.NAZWA_TABELI, CALA_TABELA);
    }


    @Override
    public boolean onCreate() {
        sqLiteOpenHelperDB = new SQLiteOpenHelperDB(getContext()); // create instance
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int typUri = sDopasowanieUri.match(uri); // check kind of uri
        SQLiteDatabase db = sqLiteOpenHelperDB.getReadableDatabase(); // open readable database
        Cursor kursor = null; // create cursor
        switch (typUri) {
            case CALA_TABELA: // case cala_tabel uri
                // get cursor to data
                kursor = db.query(true, //distinct
                        sqLiteOpenHelperDB.NAZWA_TABELI, //tabela
                        new String[]{sqLiteOpenHelperDB.ID, sqLiteOpenHelperDB.KOLUMNA1, sqLiteOpenHelperDB.KOLUMNA2, sqLiteOpenHelperDB.KOLUMNA3},
                        //kolumny
                        selection, //where
                        null, //whereArgs - argumenty zastępujące "?" w where
                        null, //group by
                        null, //having
                        null, //order by
                        null); //limit
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        //URI może być monitorowane pod kątem zmiany danych – tu jest
        //rejestrowane. Obserwator (którego trzeba zarejestrować
        //będzie powiadamiany o zmianie danych)
        kursor.setNotificationUri(getContext().getContentResolver(), uri); // bind uri to cursor
        return kursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int typUri = sDopasowanieUri.match(uri); // check kind of uri
        SQLiteDatabase db = sqLiteOpenHelperDB.getWritableDatabase(); // open writeable database
        long idDodanego = 0; // id of inserted row
        switch (typUri) {
            case CALA_TABELA:
                idDodanego = db.insert(SQLiteOpenHelperDB.NAZWA_TABELI, null, values); //insert data to database
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null); // notice about changes
        return Uri.parse(SQLiteOpenHelperDB.NAZWA_TABELI + "/" + idDodanego);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase db = sqLiteOpenHelperDB.getWritableDatabase(); //open writeable database
        int liczbaUsunietych = 0;
        switch (typUri) {
            case CALA_TABELA:
                liczbaUsunietych = db.delete(sqLiteOpenHelperDB.NAZWA_TABELI,
                        selection, //WHERE
                        selectionArgs); //usuwanie rekordów
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri, null); // notice about changes
        return liczbaUsunietych;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri); // check uri type
        SQLiteDatabase db = sqLiteOpenHelperDB.getWritableDatabase(); //open writeable database
        int update = 0;
        switch (typUri) {
            case CALA_TABELA:
                update = db.update(SQLiteOpenHelperDB.NAZWA_TABELI, values, selection, selectionArgs); // update row - return 1
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);// notice about changes
        return update;
    }
}
