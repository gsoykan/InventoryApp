package com.bucketsoft.user.project8inventoryappstageone;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreContract;
import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreDBHelper;
import com.bucketsoft.user.project8inventoryappstageone.adapters.BookstoreCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOKSTORE_LOADER = 0;

    BookstoreCursorAdapter mCursorAdapter;

    FloatingActionButton mAddBookFab;

    RelativeLayout emptyViewLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView bookListView = findViewById(R.id.bookstore_catalog_list_view);
        mAddBookFab = findViewById(R.id.add_book_fab);
        emptyViewLayout = findViewById(R.id.empty_view);

        bookListView.setEmptyView(emptyViewLayout);

        mCursorAdapter = new BookstoreCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);


        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookStoreContract.BookStoreEntry.CONTENT_URI, l);
                intent.setData(currentBookUri);
                startActivity(intent);
                Log.d("TAGX", "onItemClick: ");
            }
        });

        getLoaderManager().initLoader(BOOKSTORE_LOADER, null, this);

        mAddBookFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookCreateActivity.class);
                startActivity(intent);
            }
        });

    }


    private void insertBook() {

        ContentValues values = new ContentValues();
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, "Gurkiko");
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRICE, 13.90);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, 10);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME, "ALIS");
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE, "5350298897");


        Uri newUri = getContentResolver().insert(BookStoreContract.BookStoreEntry.CONTENT_URI, values);

        Log.d("TAGX", newUri.toString());

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                BookStoreContract.BookStoreEntry._ID,
                BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookStoreEntry.COLUMN_PRICE,
                BookStoreContract.BookStoreEntry.COLUMN_QUANTITY
        };


        return new CursorLoader(this,
                BookStoreContract.BookStoreEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
