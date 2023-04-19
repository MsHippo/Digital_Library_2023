package com.digitallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //initialize variable
    BottomNavigationView  bottomNavigation;
    private User currentUser;
    private BottomNavigationView bottomNav;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");
        Fragment fragment=null;
        Bundle bundle = new Bundle();

        //Assign variable
        bottomNav = findViewById(R.id.bottom_navigation);
        fragment = new HomeFragment();
        bundle = new Bundle();
        bundle.putSerializable("user",currentUser);//pass the value
        fragment.setArguments(bundle);
        loadFragment(fragment);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //initialize fragment
            Fragment fragment=null;
            Bundle bundle = new Bundle();

//
//                //check condition
            switch (item.getItemId()){
                case R.id.list_bullet:
                    //when id is 1
                    //initialize search fragment
                    fragment = new MyNoteFragment();
                    bundle.putSerializable("user",currentUser);//pass the value
                    fragment.setArguments(bundle);
//                    openFragment(fragment);
                    loadFragment(fragment);
                    return true;
                case R.id.favourite:
                    //when id is 2
                    //initialize home fragment
                    fragment = new FavouriteFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("user",currentUser);//pass the value
                    fragment.setArguments(bundle);
//                    openFragment(fragment);
                    loadFragment(fragment);
                    return true;
                case R.id.home:
                    //when id is 2
                    //initialize home fragment
                    fragment = new HomeFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("user",currentUser);//pass the value
                    fragment.setArguments(bundle);
//                    openFragment(fragment);
                    loadFragment(fragment);
                    return true;

                case R.id.about:
                    //when id is 3
                    //initialize about fragment
                    fragment = new AboutFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("user",currentUser);//pass the value
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.profile:
                    //when id is 4
                    //initialize profile fragment
                    fragment = new ProfileFragment();
                    bundle = new Bundle();
                    bundle.putSerializable("user",currentUser);//pass the value
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}