package com.digitallibrary;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitallibrary.FavItem.FavItem;
import com.digitallibrary.RVAdaptor.BookCallback;
import com.digitallibrary.RVAdaptor.FavAdapter;
import com.digitallibrary.databases.DBAccess;
import com.digitallibrary.model.Book;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private DBAccess DB;
    private ArrayList<FavItem> favItemList = new ArrayList<>();
    private FavAdapter favAdapter;
    private static final String TAG = FavouriteFragment.class.getSimpleName();
    private Book book;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        DB = new DBAccess(getActivity());
        recyclerView = root.findViewById(R.id.rv_book);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); // set swipe to recyclerview

        loadData();

        return root;
    }

    private void loadData() {
        if (favItemList != null) {
            favItemList.clear();
        }
        DB = DBAccess.getInstance(getContext());
        DB.open();

        Cursor cursor = DB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                int id = cursor.getInt(cursor.getColumnIndex("book_id"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                String categories = cursor.getString(cursor.getColumnIndex("categories"));
                byte[] imagesBytes = cursor.getBlob(cursor.getColumnIndex("images"));
                String url = cursor.getString(cursor.getColumnIndex("reading_url"));
                Bitmap libraryBitmap = BitmapFactory.decodeByteArray(imagesBytes, 0, imagesBytes.length);

                FavItem favItem = new FavItem(title, author,url,categories, pages, id, libraryBitmap);
                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            DB.close();
        }

        favAdapter = new FavAdapter(getActivity(), favItemList);

        recyclerView.setAdapter(favAdapter);

    }

    // remove item after swipe
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); // get position which is swipe
            final FavItem favItem = favItemList.get(position);
            if (direction == ItemTouchHelper.LEFT) { //if swipe left
                favAdapter.notifyItemRemoved(position); // item removed from recyclerview
                favItemList.remove(position); //then remove item
                DB.open();
                DB.remove_fav(favItem.getBook_id()); // remove item from database
            }
        }
    };

}