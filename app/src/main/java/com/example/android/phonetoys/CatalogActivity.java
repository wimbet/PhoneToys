package com.example.android.phonetoys;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for log messages */
    private static final String LOG_TAG = CatalogActivity.class.getName();

    /** Identifier for the phone data loader */
    private static final int PHONE_LOADER = 0;

    /** Adapter for the ListView */
    PhoneCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the phone data
        ListView phoneListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        phoneListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of phone data in the Cursor.
        // There is no phone data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new PhoneCursorAdapter(this, null);
        phoneListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        phoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i(LOG_TAG, "TEST: List item click called");
                // Create new intent to go to {@link PhoneDetailActivity}
                Intent intent = new Intent(CatalogActivity.this, PhoneDetailActivity.class);

                // Form the content URI that represents the specific phone that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PhoneEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.phones/phones/2"
                // if the phone with ID 2 was clicked on.
                Uri currentPhoneUri = ContentUris.withAppendedId(PhoneEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPhoneUri);

                // Launch the {@link PhoneDetailActivity} to display the data for the current phone.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PHONE_LOADER, null, this);
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPhone() {
        // Create a ContentValues object where column names are the keys,
        // and phone attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PhoneEntry.COLUMN_PHONE_NAME, "Google Pixel");
        values.put(PhoneEntry.COLUMN_PHONE_QUANTITY, 12);
        values.put(PhoneEntry.COLUMN_PHONE_PRICE, 649);
        values.put(PhoneEntry.COLUMN_CONTACT_INFO, "wimbet@gmail.com");
        values.put(PhoneEntry.COLUMN_PHONE_PICTURE, "exampleUri");

        // Insert a new row for Pixel phone into the provider using the ContentResolver.
        // Use the {@link PhoneEntry#CONTENT_URI} to indicate that we want to insert
        // into the phones database table.
        // Receive the new content URI that will allow us to access Pixel's data in the future.
        Uri newUri = getContentResolver().insert(PhoneEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all phones in the database.
     */
    private void deleteAllPhones() {
        int rowsDeleted = getContentResolver().delete(PhoneEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from phone database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPhone();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPhones();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PhoneEntry._ID,
                PhoneEntry.COLUMN_PHONE_NAME,
                PhoneEntry.COLUMN_PHONE_QUANTITY,
                PhoneEntry.COLUMN_PHONE_PRICE,
                PhoneEntry.COLUMN_CONTACT_INFO};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                PhoneEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PhoneCursorAdapter} with this new cursor containing updated phone data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
