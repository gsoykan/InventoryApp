package com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated;

import android.provider.BaseColumns;

public final class BookStoreContract {

    private BookStoreContract() {
    }

    public static final class BookStoreEntry implements BaseColumns {

        public final static String TABLE_NAME = "BookStoreDB";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String COLUMN_PRICE = "Price";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_SUPPLIER_NAME = "SupplierName";
        public final static String COLUMN_SUPPLIER_PHONE = "SupplierPhone";


    }
}
