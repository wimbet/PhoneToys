package com.example.android.phonetoys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

import org.w3c.dom.Text;

public class PhoneDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int PHONE_LOADER = 0;

    /** Content URI for the existing phone) */
    private Uri mCurrentPhoneUri;

    /** Adapter for the ListView */
    PhoneCursorAdapter mCursorAdapter;

    /** TextView field to display the phone's name */
    private TextView mNameTextView;

    /** TextView field to display the phone's quantity */
    private TextView mQuantityTextView;

    /** TextView field to display the phone's price */
    private TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        // Examine the intent that was used to launch this activity
        Intent intent = getIntent();
        mCurrentPhoneUri = intent.getData();

        // Find the TextViews which will be populated with the phone data
        TextView nameTextView = (TextView) findViewById(R.id.display_phone_name);
        TextView quantityTextView = (TextView) findViewById(R.id.display_phone_quantity);
        TextView priceTextView = (TextView) findViewById(R.id.display_phone_price);

        //assign the textviews
        nameTextView = mNameTextView;
        quantityTextView = mQuantityTextView;
        priceTextView = mPriceTextView;

        // Setup an Adapter to create a list item for each row of phone data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new PhoneCursorAdapter(this, null);
        //nameTextView.setAdapter(mCursorAdapter);

        // Kick off the loader
        getLoaderManager().initLoader(PHONE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PhoneEntry._ID,
                PhoneEntry.COLUMN_PHONE_NAME,
                PhoneEntry.COLUMN_PHONE_QUANTITY,
                PhoneEntry.COLUMN_PHONE_PRICE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                PhoneEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of phone attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_PRICE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);

            // Update the views on the screen with the values from the database
            mNameTextView.setText(name);
            mQuantityTextView.setText(quantity);
            mPriceTextView.setText(price);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
