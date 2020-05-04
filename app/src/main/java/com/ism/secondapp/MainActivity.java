package com.ism.secondapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ism.Infrastructure.SQLiteOpenHelperDB;
import com.ism.Infrastructure.WebsitesLoader;
import com.ism.Infrastructure.WebsitesMultiChoiceModeListener;

public class MainActivity extends AppCompatActivity {

    // declarate components
    ListView websitesLV;
    SimpleCursorAdapter adapter;
    WebsitesLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // invoke constructor from super class
        setContentView(R.layout.activity_main); // set content view
        initialize(); // initialize compontents
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // get activity menu inflater
        inflater.inflate(R.menu.toolbar, menu); // inflate toolbar from xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd: // on click add icon at toolbar menu
                Intent intent = new Intent(this, newWebsiteActivity.class);
                startActivity(intent); // run activity for add new website
                return true; // cosnsume event here
            default:
                return super.onOptionsItemSelected(item); // parent constructor
        }

    }

    private void initialize() {
        getSupportActionBar().setTitle("Baza stron"); // set the title of app bar
        websitesLV = findViewById(R.id.main_websitesLV);

        String[] mapujZ = new String[]{SQLiteOpenHelperDB.KOLUMNA1, SQLiteOpenHelperDB.KOLUMNA2, SQLiteOpenHelperDB.KOLUMNA3}; // array with name of columns
        int[] mapujDo = new int[]{R.id.lv_nameTV, R.id.lv_descTV, R.id.lv_urlTV}; // identitfiers of components to which data should be placed
        adapter = new SimpleCursorAdapter(this, R.layout.websites_row, null, mapujZ, mapujDo, 0); // create adapter

        loader = new WebsitesLoader(this, adapter); //create adapter

        websitesLV.setAdapter(adapter); // set adapter
        websitesLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL); // set ability to multiple select rows with custom toolbar
        websitesLV.setEmptyView(findViewById(R.id.main_emptyLV)); // view displayed when there is no data
        websitesLV.setMultiChoiceModeListener(new WebsitesMultiChoiceModeListener(this)); //set multi choice mode listener
        websitesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // edit website on short click
                Intent intent = new Intent(MainActivity.this, newWebsiteActivity.class); // create intent
                intent.putExtra("id", id); // pack website id to intent
                startActivity(intent); // start edit website page
            }
        });

        getLoaderManager().initLoader(0, null, loader); // set class which get callback from loader
    }

}

