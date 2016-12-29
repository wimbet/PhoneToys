package com.example.android.phonetoys;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

/**
 * Allows user to create a new phone.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** EditText field to enter the phone's name */
    private EditText mNameEditText;

    /** EditText field to enter the phone's price */
    private EditText mPriceEditText;

    /** EditText field to enter the phone's quantity */
    private EditText mQuantityEditText;

    /** EditText field to enter the supplier's contact */
    private EditText mContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_phone_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_phone_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_phone_quantity);
        mContactEditText = (EditText) findViewById(R.id.edit_phone_contact);
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private void savePhone() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String contactString = mContactEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and phone attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PhoneEntry.COLUMN_PHONE_NAME, nameString);
        values.put(PhoneEntry.COLUMN_PHONE_PRICE, priceString);
        values.put(PhoneEntry.COLUMN_PHONE_QUANTITY, quantityString);
        values.put(PhoneEntry.COLUMN_CONTACT_INFO, contactString);

        // This is a NEW phone, so insert a new phone into the provider,
        // returning the content URI for the new phone.
        Uri newUri = getContentResolver().insert(PhoneEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_phone_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_phone_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save phone to database
                savePhone();
                // Exit activity
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
