package com.digitallibrary.WishingList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitallibrary.HomeFragment;
import com.digitallibrary.R;
import com.digitallibrary.RVAdaptor.WishingAdapter;
import com.digitallibrary.User;
import com.digitallibrary.databases.DBAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WishingListActivity extends AppCompatActivity {

    RecyclerView rv_wishing;
    FloatingActionButton add_button;
    ImageButton btn_delete_all_wishing;
    private User currentUser;
    private DBAccess DB;
    ArrayList<String> book_id, book_title, book_author, book_pages, book_year;
    WishingAdapter wishingAdapter;
    ImageView iv_no_data;
    TextView tv_no_data;
    SwipeRefreshLayout sr_wishing;
    private static final String TAG = WishingListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishing_list);

        DB = DBAccess.getInstance(this);
        DB.open();
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");

        rv_wishing = findViewById(R.id.rv_wishing);
        add_button = findViewById(R.id.btn_add);
        iv_no_data = findViewById(R.id.iv_no_data);
        tv_no_data = findViewById(R.id.tv_no_data);
        btn_delete_all_wishing = findViewById(R.id.btn_delete_all_wishing);
        sr_wishing = findViewById(R.id.sr_wishing);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishingListActivity.this, AddWishingActivity.class);
                startActivity(intent);
            }
        });

        content();

        btn_delete_all_wishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        sr_wishing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

                sr_wishing.setRefreshing(false);
            }
        });

    }

    private void content() {
        DB = DBAccess.getInstance(this);
        DB.open();
        //grab data and store into array
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_year = new ArrayList<>();
        storeDataInArrays();

        //store into adapter
        wishingAdapter = new WishingAdapter(WishingListActivity.this,this, book_id, book_title, book_author,
                book_pages, book_year);
        rv_wishing.setAdapter(wishingAdapter);
        rv_wishing.setLayoutManager(new LinearLayoutManager(WishingListActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();

        }
    }

    void storeDataInArrays(){
        Cursor cursor = DB.readAllWishingData();
        if(cursor.getCount() == 0){
            //just show the image and text if there is no data
            iv_no_data.setVisibility(View.VISIBLE);
            iv_no_data.setVisibility(View.VISIBLE);
            Log.d(TAG, "No data");
        }else{
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(cursor.getColumnIndex("_id")));
                book_title.add(cursor.getString(cursor.getColumnIndex("book_title")));
                book_author.add(cursor.getString(cursor.getColumnIndex("book_author")));
                book_pages.add(cursor.getString(cursor.getColumnIndex("book_pages")));
                book_year.add(cursor.getString(cursor.getColumnIndex("book_year")));
            }
            iv_no_data.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.delete_all){
//            confirmDialog();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DB = DBAccess.getInstance(getBaseContext());
                DB.open();
                DB.deleteAllWishingData();
                //Refresh Activity
                Intent intent = new Intent(WishingListActivity.this, WishingListActivity.class);
                startActivity(intent);
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

    //make the user to refresh the content
    public void refresh(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        runnable.run();
    }
}