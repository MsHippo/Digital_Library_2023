package com.digitallibrary.RVAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.digitallibrary.FavItem.FavItem;
import com.digitallibrary.R;
import com.digitallibrary.databases.DBAccess;
import com.digitallibrary.model.Book;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FavItem> favItemList;
    private DBAccess DB;
    BookCallback callback;
//    private DatabaseReference refLike;


    public FavAdapter(Context context, ArrayList<FavItem> favItemList) {
        this.context = context;
        this.favItemList = favItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_book,
                parent, false);
        DB = DBAccess.getInstance(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FavItem favItem = favItemList.get(position);

        holder.title.setText(favItem.getTitle());
        holder.author.setText("By " + favItem.getAuthor());
        holder.categories.setText(favItem.getCategories());
        holder.pages.setText(favItem.getPages() + " pages");

        Glide.with(holder.itemView.getContext())
                .load(favItem.getImgid()) // set the img book Url
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(16)))
                .into(holder.imgBook); // destination path
    }

    @Override
    public int getItemCount() {
        return favItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBook,imgContainer;
        TextView title,author, categories, pages;
        ImageButton btn_fav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.item_book_img);
            imgContainer = itemView.findViewById(R.id.container);
            title = itemView.findViewById(R.id.item_book_title);
            author = itemView.findViewById(R.id.item_book_author);
            categories= itemView.findViewById(R.id.item_book_categories);
            pages = itemView.findViewById(R.id.item_book_pagesrev);
            btn_fav = itemView.findViewById(R.id.btn_fav);

//            refLike = FirebaseDatabase.getInstance().getReference().child("likes");
            //remove from fav after click
            btn_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavItem favItem = favItemList.get(position);
//                    final DatabaseReference upvotesRefLike = refLike.child(favItemList.get(position).getKey_id());
                    DB = DBAccess.getInstance(context);
                    DB.open();
                    DB.remove_fav(favItem.getBook_id());
                    removeItem(position);
                }
            });

        }
    }

    private void removeItem(int position) {
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,favItemList.size());
    }
}