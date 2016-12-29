package com.example.android.phonetoys;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

import org.w3c.dom.Text;

import static com.example.android.phonetoys.R.id.fab;

/**
 * Allows user to create a new phone.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for log messages */
    private static final String LOG_TAG = EditorActivity.class.getName();

    private static final int PICK_IMAGE_REQUEST = 0;

    /** EditText field to enter the phone's name */
    private EditText mNameEditText;

    /** EditText field to enter the phone's price */
    private EditText mPriceEditText;

    /** EditText field to enter the phone's quantity */
    private EditText mQuantityEditText;

    /** EditText field to enter the supplier's contact */
    private EditText mContactEditText;

    /**  field to enter the picture uri */
    private TextView mPictureText;

    //uri for add picture???
    private Uri mUri;

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTextView = (TextView) findViewById(R.id.image_uri);

        // Setup picture button for openImageSelector
        Button pictureButton = (Button) findViewById(R.id.add_picture);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSelector();
            }
        });

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_phone_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_phone_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_phone_quantity);
        mContactEditText = (EditText) findViewById(R.id.edit_phone_contact);
        mPictureText = (TextView) findViewById(R.id.image_uri);
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
        String pictureString = mPictureText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and phone attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PhoneEntry.COLUMN_PHONE_NAME, nameString);
        values.put(PhoneEntry.COLUMN_PHONE_PRICE, priceString);
        values.put(PhoneEntry.COLUMN_PHONE_QUANTITY, quantityString);
        values.put(PhoneEntry.COLUMN_CONTACT_INFO, contactString);
        values.put(PhoneEntry.COLUMN_PHONE_PICTURE, pictureString);

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
                // First validate data...
                String nameString = mNameEditText.getText().toString().trim();
                String priceString = mPriceEditText.getText().toString().trim();
                String quantityString = mQuantityEditText.getText().toString().trim();
                String pictureString = mPictureText.getText().toString().trim();
                String contactString = mContactEditText.getText().toString().trim();

                // Are the fields valid?  Then notify user they must enter a value
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.name_required),
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(priceString)) {
                    Toast.makeText(this, getString(R.string.price_required),
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(quantityString)) {
                    Toast.makeText(this, getString(R.string.quantity_required),
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(pictureString)) {
                    Toast.makeText(this, getString(R.string.picture_required),
                            Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(contactString)) {
                    Toast.makeText(this, getString(R.string.contact_required),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Save phone to database
                savePhone();
                // Exit activity
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openImageSelector() {
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

                mUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUri.toString());

                mTextView.setText(mUri.toString());
            }
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
