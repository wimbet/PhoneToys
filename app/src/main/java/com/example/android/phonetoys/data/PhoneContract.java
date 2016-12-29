package com.example.android.phonetoys.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Phone Toys app.
 */

public final class PhoneContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PhoneContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.phonetoys";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.phones/phones/ is a valid path for
     * looking at phone data. content://com.example.android.phones/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PHONES = "phones";

    /**
     * Inner class that defines constant values for the phones database table.
     * Each entry in the table represents a single phone.
     */
    public static final class PhoneEntry implements BaseColumns {

        /** The content URI to access the phone data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PHONES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of phones.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single phone.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONES;

        /** Name of database table for phones */
        public final static String TABLE_NAME = "phones";

        /**
         * Unique ID number for the phone (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the phone.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PHONE_NAME ="name";

        /**
         * Model of the phone.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PHONE_QUANTITY = "quantity";

        /**
         * Model of the phone.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PHONE_PRICE = "price";

        /**
         * Supplier contact.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CONTACT_INFO = "contact";

        /**
         * Picture uri.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PHONE_PICTURE = "picture";

    }
}