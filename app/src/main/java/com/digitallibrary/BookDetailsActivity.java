package com.digitallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitallibrary.databases.DBAccess;
import com.digitallibrary.model.Book;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookDetailsActivity extends AppCompatActivity {

    private Book book;
    DBAccess DB;
    ImageView imgBook;
    TextView title,author, categories, pages, desc;
    Button btn_read;
    ImageButton btn_fav;
    private User currentUser;
//    BottomNavigationView  bottomNavigation;
    private static final String TAG = BookDetailsActivity.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        DB = DBAccess.getInstance(this);
        DB.open();
        currentUser = (User) getIntent().getSerializableExtra("user");

        book = (Book) getIntent().getSerializableExtra("bookObject");

        imgBook = findViewById(R.id.item_book_img);
        title = findViewById(R.id.item_book_title);
        author = findViewById(R.id.item_book_author);
        categories= findViewById(R.id.item_book_categories);
        pages = findViewById(R.id.item_book_pagesrev);
        desc = findViewById(R.id.details_desc);
        btn_read = findViewById(R.id.btn_read);
        btn_fav = findViewById(R.id.btn_fav);


//        imgBook.setImageBitmap(book.getImgid());
        title.setText(book.getTitle());
        author.setText("By " + book.getAuthor());
        categories.setText(book.getCategories());
        pages.setText(book.getPages() + " pages");
        desc.setText(book.getDescription());

        if (book.getFav_status().equals("1")){
            btn_fav.setBackgroundResource(R.drawable.ic_favorite_red);
        }else {
            btn_fav.setBackgroundResource(R.drawable.ic_favorite_grey);
        }

        //button listener
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(book.getImgUrl()));
                startActivity(intent);
            }
        });

        //ini view
//        imgBook = findViewById(R.id.item_book_img);

        // we need to get book item object

//        Book item = (Book) getIntent().getSerializableExtra("bookObject");
//        Book item = (Book) getIntent().getSerializableExtra("bitmapImages");

        //Retrieve the bitmap passed from FindBooks activity.
        Bitmap bmp;
        byte[] byteArray = getIntent().getByteArrayExtra("bitmapImages");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        loadBookData(bmp);

    }

    private void loadBookData(Bitmap item) {

        // bind book data here for now i will only load the book cover image

        Glide.with(this).load(item).into(imgBook);

    }
}