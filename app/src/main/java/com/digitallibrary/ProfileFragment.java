package com.digitallibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitallibrary.databases.DBAccess;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private User currentUser;
    TextView txt_name;
    TextView txt_email;
    TextView btn_edit_profile;
    DBAccess DB;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    SwipeRefreshLayout sr_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        DB = DBAccess.getInstance(getContext());
        DB.open();

        txt_name = v.findViewById(R.id.txt_name);
        txt_email = v.findViewById(R.id.txt_email);
        btn_edit_profile = v.findViewById(R.id.btn_edit_profile);
        sr_setting = v.findViewById(R.id.sr_setting);

        //get intent for current user
        Bundle bundle = getArguments();
        currentUser = (User) bundle.getSerializable("user");

        content();

        btn_edit_profile.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.btn_edit_profile:
                i = new Intent(getActivity(), EditProfile.class);
                i.putExtra("user", currentUser);
                startActivity(i);
                break;
        }
    }

    public void content(){
        DB.open();
        StringBuffer buffer = new StringBuffer();
        Cursor res = DB.getLatestUser(currentUser.getId());
//        res.getString(res.getColumnIndex("user_name"));

        while (res.moveToNext()){
            String name = buffer.append(
                    res.getString(res.getColumnIndex("user_name"))
            )+"";
            buffer.delete(0,res.getString(res.getColumnIndex("user_name")).length());
            String email = buffer.append(
                    res.getString(res.getColumnIndex("user_email"))
            )+"\n";

            txt_name.setText(name);
            txt_email.setText(email);
        }

        sr_setting.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                
                sr_setting.setRefreshing(false);
            }
        });
    }

    //make the user to refresh the content
    public void refresh(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        runnable.run();
    }

}
