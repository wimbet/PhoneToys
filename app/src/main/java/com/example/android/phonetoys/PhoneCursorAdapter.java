package com.example.android.phonetoys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

/**
 * {@link PhoneCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of phone data as its data source. This adapter knows
 * how to create list items for each row of phone data in the {@link Cursor}.
 */
public class PhoneCursorAdapter extends CursorAdapter {

    /** Tag for log messages */
    private static final String LOG_TAG = PhoneCursorAdapter.class.getName();

    /** Identifier for the phone data loader */
    private static final int EXISTING_PHONE_LOADER = 0;

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
     * Method to decrease the quantity by 1.
     */
    private void decreaseQuantity() {

    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        Button sellButton = (Button) view.findViewById(R.id.sell_button);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.i(LOG_TAG, "TEST: Sell onClick called");
                decreaseQuantity();

            }
        });

        // Find the columns of phone attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_PHONE_QUANTITY);
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
