package com.example.android.phonetoys;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

import org.w3c.dom.Text;

import static com.example.android.phonetoys.data.PhoneContract.PhoneEntry.COLUMN_CONTACT_INFO;
import static com.example.android.phonetoys.data.PhoneContract.PhoneEntry.COLUMN_PHONE_NAME;
import static com.example.android.phonetoys.data.PhoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY;

public class PhoneDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for log messages */
    private static final String LOG_TAG = PhoneDetailActivity.class.getName();

    /**
     * Identifier for the pet data loader
     */
    private static final int PHONE_LOADER = 0;

    /**
     * Content URI for the existing phone)
     */
    private Uri mCurrentPhoneUri;

    /**
     * TextView field to display the phone's name
     */
    private TextView mNameTextView;

    /**
     * TextView field to display the phone's quantity
     */
    private TextView mQuantityTextView;

    /**
     * TextView field to display the phone's price
     */
    private TextView mPriceTextView;

    /**
     * TextView field to display the supplier's contact
     */
    private TextView mContactTextView;

    /**
     * ImageView field to display the phone's picture
     */
    private ImageView mPictureImageView;

    private TextView mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        // Examine the intent that was used to launch this activity
        Intent intent = getIntent();
        mCurrentPhoneUri = intent.getData();

        // Find the views which will be populated with the phone data
        mNameTextView = (TextView) findViewById(R.id.display_phone_name);
        mQuantityTextView = (TextView) findViewById(R.id.display_phone_quantity);
        mPriceTextView = (TextView) findViewById(R.id.display_phone_price);
        mContactTextView = (TextView) findViewById(R.id.display_contact_info);
        mPictureImageView = (ImageView) findViewById(R.id.display_picture);

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
                PhoneEntry.COLUMN_PHONE_PRICE,
                PhoneEntry.COLUMN_PHONE_PICTURE,
                PhoneEntry.COLUMN_CONTACT_INFO};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentPhoneUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of phone attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_PRICE);
            int contactColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_CONTACT_INFO);
            int pictureColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_PICTURE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String contact = cursor.getString(contactColumnIndex);
            String picture = cursor.getString(pictureColumnIndex);

            // Update the views on the screen with the values from the database
            mNameTextView.setText(name);
            mQuantityTextView.setText(Integer.toString(quantity));
            mPriceTextView.setText(Integer.toString(price));
            mContactTextView.setText(contact);
            mPictureImageView.setImageURI(Uri.parse(picture));
        }

        // Find the buttons which will be clicked on
        Button minusButton = (Button) findViewById(R.id.minus_button);
        Button plusButton = (Button) findViewById(R.id.plus_button);
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        Button orderButton = (Button) findViewById(R.id.order_button);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Minus onClick called");

                //get the Uri for the current phone
                int itemIdColumnIndex = cursor.getColumnIndex(PhoneEntry._ID);
                final long itemId = cursor.getLong(itemIdColumnIndex);
                Uri mCurrentPhoneUri = ContentUris.withAppendedId(PhoneEntry.CONTENT_URI, itemId);

                // Find the columns of phone attributes that we're interested in
                int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PHONE_QUANTITY);

                //read the phone attributes from the Cursor for the current phone
                String phoneQuantity = cursor.getString(quantityColumnIndex);

                //convert the string to an integer
                int updateQuantity = Integer.parseInt(phoneQuantity);

                if (updateQuantity > 0) {

                    //decrease the quantity by 1
                    updateQuantity--;

                    // Defines an object to contain the updated values
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(PhoneEntry.COLUMN_PHONE_QUANTITY, updateQuantity);

                    //update the phone with the content URI mCurrentPhoneUri and pass in the new
                    //content values. Pass in null for the selection and selection args
                    //because mCurrentPhoneUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsUpdate = getContentResolver().update(mCurrentPhoneUri, updateValues, null, null);
                } else {
                    Toast.makeText(PhoneDetailActivity.this, "Quantity is 0 and can't be reduced.", Toast.LENGTH_SHORT).show();
                };

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Plus onClick called");

                //get the Uri for the current phone
                int itemIdColumnIndex = cursor.getColumnIndex(PhoneEntry._ID);
                final long itemId = cursor.getLong(itemIdColumnIndex);
                Uri mCurrentPhoneUri = ContentUris.withAppendedId(PhoneEntry.CONTENT_URI, itemId);

                // Find the columns of phone attributes that we're interested in
                int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PHONE_QUANTITY);

                //read the phone attributes from the Cursor for the current phone
                String phoneQuantity = cursor.getString(quantityColumnIndex);

                //convert the string to an integer
                int updateQuantity = Integer.parseInt(phoneQuantity);

                //increase the quantity by 1
                updateQuantity++;

                // Defines an object to contain the updated values
                ContentValues updateValues = new ContentValues();
                updateValues.put(PhoneEntry.COLUMN_PHONE_QUANTITY, updateQuantity);

                //update the phone with the content URI mCurrentPhoneUri and pass in the new
                //content values. Pass in null for the selection and selection args
                //because mCurrentPhoneUri will already identify the correct row in the database that
                // we want to modify.
                int rowsUpdate = getContentResolver().update(mCurrentPhoneUri, updateValues, null, null);
                };
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Delete onClick called");

                // Create an AlertDialog.Builder and set the message, and click listeners
                // for the positive and negative buttons on the dialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(PhoneDetailActivity.this);
                builder.setMessage(R.string.delete_dialog_msg);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Delete" button, so delete the pet.
                        //get the Uri for the current phone
                        int itemIdColumnIndex = cursor.getColumnIndex(PhoneEntry._ID);
                        final long itemId = cursor.getLong(itemIdColumnIndex);
                        Uri mCurrentPhoneUri = ContentUris.withAppendedId(PhoneEntry.CONTENT_URI, itemId);

                        //delete the current phone ID
                        int rowsDeleted = getContentResolver().delete(mCurrentPhoneUri, null, null);

                        // Close the activity
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Cancel" button, so dismiss the dialog
                        // and continue editing the pet.
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Order onClick called");

                //get the Uri for the current phone
                int itemIdColumnIndex = cursor.getColumnIndex(PhoneEntry._ID);
                final long itemId = cursor.getLong(itemIdColumnIndex);
                Uri mCurrentPhoneUri = ContentUris.withAppendedId(PhoneEntry.CONTENT_URI, itemId);

                // Find the columns of phone attributes that we're interested in
                int contactColumnIndex = cursor.getColumnIndex(COLUMN_CONTACT_INFO);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_PHONE_NAME);

                //read the phone attributes from the Cursor for the current phone
                String phoneContact = cursor.getString(contactColumnIndex);
                String[] emailTo = {phoneContact};

                //read the phone name to use in subject line
                String phoneName = cursor.getString(nameColumnIndex);
                String subjectLine = "Need to order: " + phoneName;

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, emailTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectLine);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            };
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
