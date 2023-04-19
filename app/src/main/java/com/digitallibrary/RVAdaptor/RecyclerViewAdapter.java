package com.digitallibrary.RVAdaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digitallibrary.R;
import com.digitallibrary.model.RecyclerData;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<RecyclerData> libraryDataArrayList;
    private Context mcontext;

    public RecyclerViewAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.libraryDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }
//    public RecyclerViewAdapter(ArrayList<RecyclerData> libraryDataArrayList) {
//        this.libraryDataArrayList = libraryDataArrayList;
//    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        RecyclerData recyclerData = libraryDataArrayList.get(position);

        holder.libraryTV.setText(recyclerData.getTitle());
        holder.libraryIV.setImageBitmap(recyclerData.getImgid());
        holder.libraryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recyclerData.getLink()));
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return libraryDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView libraryTV;
        private ImageView libraryIV;
        private CardView libraryCard;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            libraryTV = itemView.findViewById(R.id.id_library_name);
            libraryIV = itemView.findViewById(R.id.id_library_image);
            libraryCard = itemView.findViewById(R.id.btn_card_library);
        }
    }
}