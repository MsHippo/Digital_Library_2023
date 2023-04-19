package com.digitallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.digitallibrary.RVAdaptor.RecyclerViewAdapter;
import com.digitallibrary.databases.DBAccess;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MalaysiaLibrary extends AppCompatActivity {

    //initialize variable
    BottomNavigationView  bottomNavigation;
    private User currentUser;
    DBAccess DB;
    private static final String TAG = MalaysiaLibrary.class.getSimpleName();
    private RecyclerView recyclerView;
//    private ArrayList<RecyclerData> recyclerDataArrayList;

    private RecyclerViewAdapter libraryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malaysia_library);
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");

        recyclerView=findViewById(R.id.library_rv);

        DB = DBAccess.getInstance(this);
        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.

        getData();


    }

    private void loadFragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    public void getData(){
        try {
            DB.open();
            libraryAdapter = new RecyclerViewAdapter( DB.getAllImagesData(), this);
            recyclerView.setHasFixedSize(true);

            GridLayoutManager layoutManager=new GridLayoutManager(this,2);

            // at last set adapter to recycler view.
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(libraryAdapter);
        }
        catch (Exception e){
            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

}