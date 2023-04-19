package com.digitallibrary.WishingList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digitallibrary.HomeFragment;
import com.digitallibrary.R;
import com.digitallibrary.User;
import com.digitallibrary.databases.DBAccess;

public class AlterWishingActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input, year_input;
    Button update_button, delete_button;
    String id, title, author, pages, year;
    private DBAccess DB;
    private User currentUser;
    private static final String TAG = AlterWishingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_wishing);

        DB = DBAccess.getInstance(this);
        DB.open();
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");

        title_input = findViewById(R.id.etx_update_book_title);
        author_input = findViewById(R.id.etx_update_author);
        pages_input = findViewById(R.id.etx_update_pages);
        year_input = findViewById(R.id.etx_update_year);
        update_button = findViewById(R.id.btn_update_info);
        delete_button = findViewById(R.id.btn_delete_info);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                title = title_input.getText().toString().trim();
                author = author_input.getText().toString().trim();
                pages = pages_input.getText().toString().trim();
                year = year_input.getText().toString().trim();
                DB.updateWishingData(id, title, author, pages, year);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    public void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("pages") &&
                getIntent().hasExtra("year")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            year = getIntent().getStringExtra("year");

            //Setting Intent Data
            title_input.setText(title);
            author_input.setText(author);
            pages_input.setText(pages);
            year_input.setText(year);
            Log.d(TAG, title+" "+author+" "+pages + " "+ year);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DB = DBAccess.getInstance(getBaseContext());
                DB.open();
                DB.deleteOneWishingRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}