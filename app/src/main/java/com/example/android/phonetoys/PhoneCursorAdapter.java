package com.example.android.phonetoys;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonetoys.data.PhoneContract.PhoneEntry;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.phonetoys.data.PhoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY;

/**
 * {@link PhoneCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of phone data as its data source. This adapter knows
 * how to create list items for each row of phone data in the {@link Cursor}.
 */
public class PhoneCursorAdapter extends CursorAdapter {

    /** Tag for log messages */
    private static final String LOG_TAG = PhoneCursorAdapter.class.getName();

    /**
     * Constructs a new {@link PhoneCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PhoneCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the phone data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        Button sellButton = (Button) view.findViewById(R.id.sell_button);

        //get the position before the button is clicked
        final int position = cursor.getPosition();

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Sell onClick called");

                //move the cursor to the correct position
                cursor.moveToPosition(position);

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
                    int rowsUpdate = context.getContentResolver().update(mCurrentPhoneUri, updateValues, null, null);
                } else {
                    Toast.makeText(context, "Quantity is 0 and can't be reduced.", Toast.LENGTH_SHORT).show();
                };

            }
        });

        // Find the columns of phone attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PHONE_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_PRICE);

        // Read the phone attributes from the Cursor for the current phone
        String phoneName = cursor.getString(nameColumnIndex);
        String phoneQuantity = cursor.getString(quantityColumnIndex);
        String phonePrice = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes for the current phone
        nameTextView.setText(phoneName);
        quantityTextView.setText(phoneQuantity);
        priceTextView.setText(phonePrice);
    }

}
