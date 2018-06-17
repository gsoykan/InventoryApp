package com.bucketsoft.user.project8inventoryappstageone.adapters;

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

import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreContract;
import com.bucketsoft.user.project8inventoryappstageone.R;

import org.w3c.dom.Text;

public class BookstoreCursorAdapter extends android.support.v4.widget.CursorAdapter {

    public BookstoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.book_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView bookNameTextView = view.findViewById(R.id.book_name_list_item);
        TextView bookPriceTextView = view.findViewById(R.id.book_price_list_item);
        final TextView bookQuantityTextView = view.findViewById(R.id.book_quantity_list_item);
        Button bookSaleButton = view.findViewById(R.id.book_sale_button_list_item);

        int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry._ID);


        final String bookName = cursor.getString(nameColumnIndex);
        Double bookPrice = cursor.getDouble(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);
        final long id = cursor.getLong(idColumnIndex);


        bookNameTextView.setText(bookName);
        bookPriceTextView.setText(String.valueOf(bookPrice));
        bookQuantityTextView.setText(String.valueOf(bookQuantity));

        Log.d("TAG", bookName);

        bookSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookQuantity < 1) {
                    Toast.makeText(context, R.string.book_quantity_button_toast_message, Toast.LENGTH_LONG).show();
                } else {
                    int prevQuantity = Integer.valueOf(bookQuantityTextView.getText().toString());
                    int newQuantity = prevQuantity - 1;
                    ContentValues values = new ContentValues();
                    values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, newQuantity);
                    Uri currentBookUri = ContentUris.withAppendedId(BookStoreContract.BookStoreEntry.CONTENT_URI, id);
                    int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);
                    if (rowsAffected == 1) {
                        Toast.makeText(context, R.string.book_sold_success, Toast.LENGTH_LONG).show();
                        bookQuantityTextView.setText(String.valueOf(newQuantity));
                    }


                }
            }
        });

    }


}
