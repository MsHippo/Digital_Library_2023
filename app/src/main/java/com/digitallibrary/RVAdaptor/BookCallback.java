package com.digitallibrary.RVAdaptor;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public interface BookCallback extends Serializable {

    void onBookItemClick(int pos,
                         ImageView imgContainer,
                         ImageView imgBook,
                         TextView title,
                         TextView authorName,
                         TextView categories,
                         TextView nbpages);
}