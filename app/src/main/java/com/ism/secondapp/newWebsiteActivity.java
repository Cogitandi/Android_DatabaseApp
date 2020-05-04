package com.ism.secondapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.ism.Infrastructure.ContentProviderDB;
import com.ism.Infrastructure.FormTextWatcher;
import com.ism.Infrastructure.SQLiteOpenHelperDB;

public class newWebsiteActivity extends AppCompatActivity {

    // declarate components
    Button cancelBTN;
    Button saveBTN;
    FormEditText webNameET;
    FormEditText descriptionET;
    FormEditText urlET;
    long receivedId;
    ImageView pdfIV;
    ImageView internetIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_website);
        initialize(); // initialize components
        receivedId = getIntent().getLongExtra("id", 0); // get id of received website - edit mode or 0 when create new website
        modifyWebsite(); // check and modify received website
    }

    private void initialize() {
        // initialize components
        cancelBTN = findViewById(R.id.newWebsite_cancelBTN);
        saveBTN = findViewById(R.id.newWebsite_addBTN);
        webNameET = findViewById(R.id.newWebsite_webNameET);
        descriptionET = findViewById(R.id.newWebsite_descriptionET);
        urlET = findViewById(R.id.newWebsite_urlET);
        pdfIV = findViewById(R.id.pdfIV);
        internetIV = findViewById(R.id.internetIV);

        // walidate form text watchers
        webNameET.addTextChangedListener(new FormTextWatcher(webNameET));
        descriptionET.addTextChangedListener(new FormTextWatcher(descriptionET));
        urlET.addTextChangedListener(new FormTextWatcher(urlET, pdfIV, internetIV));

        clickActions(); // actions of click buttons/icons
        getSupportActionBar().setTitle("Dodaj stronę"); // set title of app bar
    }

    private void clickActions() {
        // action on click cancel button
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // action on click save button
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWebsite();
            }
        });
        // action on click pdf icon
        pdfIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!urlET.testValidity()) return; // check is website is correct
                Intent pdf = new Intent("android.intent.action.VIEW", Uri.parse("https://api.html2pdf.app/v1/generate?url=" + urlET.getText().toString() + "&apiKey=8fc8992e7ab59463faeffa82343b41a21bb05a1fe5bc2102c52a02e0a171b864"));
                startActivity(pdf);
            }
        });
        // action on click website icon
        internetIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!urlET.testValidity()) return; // check is website is correct
                Intent www = new Intent("android.intent.action.VIEW", Uri.parse(urlET.getText().toString()));
                startActivity(www);
            }
        });
    }

    private void addWebsite() {
        if (!allValid()) { // check if all fields are correct
            new Toast(this).makeText(this, "Wypełnij pola poprawnie", Toast.LENGTH_SHORT).show(); // if no show toast
            return;
        }
        // get text from fields
        String websiteName = webNameET.getText().toString();
        String description = descriptionET.getText().toString();
        String url = urlET.getText().toString();

        // create values which will be inserted into database
        ContentValues values = new ContentValues();
        values.put(SQLiteOpenHelperDB.KOLUMNA1, websiteName);
        values.put(SQLiteOpenHelperDB.KOLUMNA2, description);
        values.put(SQLiteOpenHelperDB.KOLUMNA3, url);

        if (receivedId != 0) { // if there is modify mode
            getContentResolver().update(ContentProviderDB.URI_ZAWARTOSCI, values, SQLiteOpenHelperDB.ID + "=" + receivedId, null);
        } else { // if there is new website
            getContentResolver().insert(ContentProviderDB.URI_ZAWARTOSCI, values);
        }
        finish(); // close activity
    }

    // function which check is all fields correct
    public boolean allValid() {
        FormEditText[] allFields = {webNameET, descriptionET, urlET}; // fields which will be checked
        boolean allValid = true;
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
        }
        return allValid;
    }

    // modify mode
    public void modifyWebsite() {
        if (receivedId != 0) { // check whether got id
            getSupportActionBar().setTitle("Modyfikuj stronę"); // change title of app bar

            // get data abotu website from database
            Cursor cursor = getContentResolver().query(ContentProviderDB.URI_ZAWARTOSCI, null, SQLiteOpenHelperDB.ID + "=" + receivedId, null, null);
            cursor.moveToFirst(); // move to data row
            // set data from database
            webNameET.setText(cursor.getString(cursor.getColumnIndex(SQLiteOpenHelperDB.KOLUMNA1)));
            descriptionET.setText(cursor.getString(cursor.getColumnIndex(SQLiteOpenHelperDB.KOLUMNA2)));
            urlET.setText(cursor.getString(cursor.getColumnIndex(SQLiteOpenHelperDB.KOLUMNA3)));
            // enable buttons
            pdfIV.setEnabled(true);
            internetIV.setEnabled(true);
        }
    }

}
