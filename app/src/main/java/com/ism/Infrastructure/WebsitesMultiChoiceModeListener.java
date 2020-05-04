package com.ism.Infrastructure;

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.ism.secondapp.R;

import java.util.ArrayList;
import java.util.List;

public class WebsitesMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    private Activity activity; // activity associated with this listener
    private List<Long> selectedIds = new ArrayList<>(); // array which store selected ids

    public WebsitesMultiChoiceModeListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // invoke on select or unselect website
        if (checked) {
            selectedIds.add(new Long(id)); // add selected id to array on select
        } else {
            selectedIds.remove(new Long(id)); // remove id from array on unselect
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = activity.getMenuInflater(); // get menu inflater
        inflater.inflate(R.menu.main_remove, menu); // inflate toolbar which appear on select item
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionRemove: // on click remove button
                deleteWebsites(); // remove selected websites from database
                return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private void deleteWebsites() {
        for (Long item : selectedIds) { // for each website
            // remove from database
            activity.getContentResolver().delete(ContentProviderDB.URI_ZAWARTOSCI, SQLiteOpenHelperDB.ID + "=" + item, null);
        }

    }
}
