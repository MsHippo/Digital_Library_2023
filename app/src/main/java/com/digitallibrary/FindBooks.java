package com.digitallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.core.util.Pair;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import com.digitallibrary.RVAdaptor.BookAdapter;
import com.digitallibrary.RVAdaptor.BookCallback;
import com.digitallibrary.databases.DBAccess;
import com.digitallibrary.model.Book;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FindBooks extends AppCompatActivity implements BookCallback, SearchView.OnQueryTextListener{

    //initialize variable
    private User currentUser;
    private static final String TAG = FindBooks.class.getSimpleName();
    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private List<Book> mdata ;
    private Book book;
    DBAccess DB;
    SearchView search_btn;

//    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");
        DB = DBAccess.getInstance(this);

        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnQueryTextListener(this);
        initViews();
//        initmdataBooks();
        setupBookAdapter();

    }


    private void initViews() {

        rvBooks = findViewById(R.id.rv_book);
//        rvBooks.setLayoutManager(new LinearLayoutManager(this));
//        rvBooks.setHasFixedSize(true);
        // we need first to setup the custom item animator that we just create

    }

    private void setupBookAdapter() {
        DB.open();
        bookAdapter = new BookAdapter(DB.getAllBookData(),this, getBaseContext());

        rvBooks.setHasFixedSize(true);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        rvBooks.setAdapter(bookAdapter);
    }

    @Override
    public void onBookItemClick(int pos,
                                ImageView imgContainer,
                                ImageView imgBook,
                                TextView title,
                                TextView authorName,
                                TextView categories,
                                TextView nbpages) {

//        mdata = DB.checkCorrespondingBook((pos+1));

        // create intent and send book object to Details activity

        Intent intent = new Intent(this,BookDetailsActivity.class);
//        intent.putExtra("bookObject",book);
//        intent.putExtra("bookObject",mdata.get(pos));

        DB.open();
        book = DB.checkCorrespondingBook(pos);
        Log.d(TAG, "pos: " + pos);
//        Log.d(TAG, "DB.getAllBookData().get(pos): " + DB.getAllBookData().get(pos).toString());

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bookObject", book);

        intent.putExtra("bookObject", (Parcelable) book);
        intent.putExtra("user",currentUser);

        //Pass Bitmap as Extended Data
        Bitmap bitmap = book.getImgid();
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();
        intent.putExtra("bitmapImages", byteArray);

        // shared Animation setup
        // let's import the Pair class

        Pair<View,String> p1 = Pair.create(imgContainer,"containerTN"); // second arg is the tansition string Name
        Pair<View,String> p2 = Pair.create(imgBook,"bookTN"); // second arg is the tansition string Name
        Pair<View,String> p3 = Pair.create(title,"booktitleTN"); // second arg is the tansition string Name
        Pair<View,String> p4 = Pair.create(authorName,"authorTN"); // second arg is the tansition string Name
        Pair<View,String> p5 = Pair.create(nbpages,"bookpagesTN"); // second arg is the tansition string Name
        Pair<View,String> p6 = Pair.create(categories,"categoriesTN"); // second arg is the tansition string Name


        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,p1,p2,p3,p4,p5,p6);

//        Bundle bundle = optionsCompat.toBundle();
//        bundle.putSerializable("bookObject", book);
        // start the activity with scene transition

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent,optionsCompat.toBundle());
        }
        else{
            startActivity(intent);
        }

    }

    //searching
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        bookAdapter.getFilter().filter(newText);
        return false;
    }
}