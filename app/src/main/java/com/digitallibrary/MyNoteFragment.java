package com.digitallibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digitallibrary.WishingList.AddWishingActivity;
import com.digitallibrary.WishingList.WishingListActivity;

public class MyNoteFragment extends Fragment implements View.OnClickListener {

    private User currentUser;
    CardView btn_wishing_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_note, container, false);
        Bundle bundle = getArguments();
        currentUser = (User) bundle.getSerializable("user");
        btn_wishing_list = v.findViewById(R.id.btn_wishing_list);
        btn_wishing_list.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn_wishing_list:
                intent = new Intent(getActivity(), WishingListActivity.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("Username", currentUser.getUsername());
                startActivity(intent);
                break;
        }
    }
}