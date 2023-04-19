package com.digitallibrary.RVAdaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.digitallibrary.R;
import com.digitallibrary.databases.DBAccess;
import com.digitallibrary.model.Book;

import java.util.ArrayList;
import java.util.Collection;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.bookviewholder> implements Filterable {

    ArrayList<Book> mdata;
    ArrayList<Book> mdataList;
    private Context context;
    BookCallback callback;
    private DBAccess DB;


    public BookAdapter(ArrayList<Book> mdata, BookCallback callback, Context context) {
        this.mdata = mdata;
        this.callback = callback;
        this.context = context;
        this.mdataList = new ArrayList<>(mdata);
    }


    @NonNull
    @Override
    public bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DB = new DBAccess(context);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        return new bookviewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull bookviewholder holder, int position) {

        // bind book item data here
        // I'm just going to bint the book image..

        //load book image using Glide
        Book book = mdata.get(position);
        readCursorData(book, holder);

        holder.title.setText(book.getTitle());
        holder.author.setText("By " + book.getAuthor());
        holder.categories.setText(book.getCategories());
        holder.pages.setText(book.getPages() + " pages");

        Glide.with(holder.itemView.getContext())
                .load(book.getImgid()) // set the img book Url
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.imgBook); // destination path


    }

    private void readCursorData(Book book, bookviewholder viewHolder) {
        Log.d("Book id", ": " + book.getBook_id());
        DB = DBAccess.getInstance(context);
        DB.open();

        Cursor cursor = DB.read_fav_data(book.getBook_id());

        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex("fav_status"));
                book.setFav_status(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.btn_fav.setBackgroundResource(R.drawable.ic_favorite_red);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.btn_fav.setBackgroundResource(R.drawable.ic_favorite_grey);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            DB.close();
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    //setting the filter function
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on the background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Book> filterList = new ArrayList<>();

            //put all the information into list
            //get the title of the book in lower case, easy for searching
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(mdataList);
            } else {
                for (Book mdataItem : mdataList) {
                    if (mdataItem.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())
                    || mdataItem.getAuthor().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filterList.add(mdataItem);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        //run on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mdata.clear();
            mdata.addAll((Collection<? extends Book>) results.values);
            notifyDataSetChanged();
        }
    };

    public class bookviewholder extends RecyclerView.ViewHolder {

        ImageView imgBook, imgContainer;
        TextView title, author, categories, pages;
        ImageButton btn_fav;

        public bookviewholder(@NonNull View itemView) {
            super(itemView);


            imgBook = itemView.findViewById(R.id.item_book_img);
            imgContainer = itemView.findViewById(R.id.container);
            title = itemView.findViewById(R.id.item_book_title);
            author = itemView.findViewById(R.id.item_book_author);
            categories = itemView.findViewById(R.id.item_book_categories);
            pages = itemView.findViewById(R.id.item_book_pagesrev);
            btn_fav = itemView.findViewById(R.id.btn_fav);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DB.open();
                    int position = getAdapterPosition();
                    Book book = mdata.get(position);

                    callback.onBookItemClick(book.getBook_id(),
                            imgContainer,
                            imgBook,
                            title,
                            author,
                            categories,
                            pages);
                }
            });

            btn_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DB.open();
                    int position = getAdapterPosition();
                    Book book = mdata.get(position);

                    if (book.getFav_status().equals("0")) {
                        book.setFav_status("1");
                        DB.update_fav(book.getBook_id());
                        btn_fav.setBackgroundResource(R.drawable.ic_favorite_red);
                    } else {
                        book.setFav_status("0");
                        DB.remove_fav(book.getBook_id());
                        btn_fav.setBackgroundResource(R.drawable.ic_favorite_grey);
                    }
                }
            });


        }
    }
}