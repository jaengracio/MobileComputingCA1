package com.bookstore.ca1bookstore;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;
/*  The MainActivity is what the user sees first
    when the app is launched. It contains a list of books
    that you can choose from and a menu bar with buttons. */
public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListener {

    private boolean mTwoPane = false;
    private Book mSelectedBook = null;

    @Override
    /*  onCreate launches the app and displays a list of books
        by setting the view to a layout file. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bookstore");

        int savedBookId = -1;
        if (savedInstanceState != null) {
            savedBookId = savedInstanceState.getInt(BookDetailFragment.EXTRA_BOOK_ID, -1);
        }

        List<Book> books = Model.getInstance(this).getBooks();
        if (!books.isEmpty()) {
            if (savedBookId == -1) {
                mSelectedBook = Model.getInstance(this).getBooks().get(0);
            } else {
                mSelectedBook = Model.getInstance(this).findBookById(savedBookId);
            }
        }

        mTwoPane = (findViewById(R.id.book_details_two_pane) != null);
        if (mTwoPane && mSelectedBook != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.book_details_two_pane, BookDetailFragment.newInstance(mSelectedBook.getId()));
            ft.commit();
        }
    }

    /*  This displays the menu bar so users can select the
        shopping cart button or the settings button */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        return true;
    }

    /*  This helps the user to know if they clicked a button in the menu bar
        by displaying a message */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shoppingCart:
                Toast.makeText(this, "Shopping Cart selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*  When a book is selected, a new activity will start
        or the book details will show up as a fragment
        if the screen is bigger i.e. Tablet mode */
    @Override
    public void itemSelected(Book b) {
        mSelectedBook = b;
        if (mTwoPane) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.book_details_two_pane, BookDetailFragment.newInstance(b.getId()));
            ft.commit();
        }
        else {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.EXTRA_BOOK_ID, b.getId());
            startActivity(intent);
        }
    }

    /*  This retrieves the id of the book you selected from the list */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mSelectedBook != null) {
            savedInstanceState.putInt(BookDetailFragment.EXTRA_BOOK_ID, mSelectedBook.getId());
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}
