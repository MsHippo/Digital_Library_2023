package com.digitallibrary.WishingList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitallibrary.R;
import com.digitallibrary.User;
import com.digitallibrary.databases.DBAccess;

public class AddWishingActivity extends AppCompatActivity {

    EditText etx_book_title, etx_author, etx_pages, etx_year;
    Button btn_add_info;
    private DBAccess DB;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wishing);

        DB = DBAccess.getInstance(this);
        DB.open();
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");

        etx_book_title = findViewById(R.id.etx_book_title);
        etx_author = findViewById(R.id.etx_author);
        etx_pages = findViewById(R.id.etx_pages);
        etx_year = findViewById(R.id.etx_year);

        btn_add_info = findViewById(R.id.btn_add_info);
        btn_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.addWishingBook(etx_book_title.getText().toString().trim(),
                        etx_author.getText().toString().trim(),
                        Integer.parseInt(etx_pages.getText().toString().trim()),
                        Integer.parseInt(etx_year.getText().toString().trim()));
            }
        });

    }
}