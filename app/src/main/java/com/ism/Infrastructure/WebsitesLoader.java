package com.ism.Infrastructure;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class WebsitesLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Activity activity; // activity associated with loader
    private SimpleCursorAdapter adapter;

    public WebsitesLoader(Activity activity, SimpleCursorAdapter adapter) {
        // initialization
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create cursor
        String[] projekcja = {SQLiteOpenHelperDB.KOLUMNA1, SQLiteOpenHelperDB.KOLUMNA2, SQLiteOpenHelperDB.KOLUMNA3}; // column for display
        // create laoder
        CursorLoader loaderKursora = new CursorLoader(activity, ContentProviderDB.URI_ZAWARTOSCI, projekcja, null, null, null);
        return loaderKursora;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data); // swap cursor with new data cursor
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null); // swap cursor with new cursor null data -
    }

}
