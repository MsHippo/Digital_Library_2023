package com.digitallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.digitallibrary.databases.DBAccess;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = EditProfile.this;
    //initialize variable
    private ConstraintLayout constraintLayout;
    private TextInputLayout textInputLayoutName;
    //    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextName;
    //    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private Button btn_save_profile;
    private InputValidation inputValidation;
    Fragment fragment = null;

    private User currentUser;

    //    BottomNavigationView  bottomNavigation;
    DBAccess DB;
    private static final String TAG = EditProfile.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        DB = DBAccess.getInstance(this);
        DB.open();
        //get intent for current user
        currentUser = (User) getIntent().getSerializableExtra("user");

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textProfileLayoutName);
//        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textProfileLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textProfileLayoutPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textProfileEditTextName);
//        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textProfileEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textProfileEditTextPassword);

        btn_save_profile = findViewById(R.id.btn_save_profile);

        content();

//        bottomNavigation();
    }

    public void content() {
        StringBuffer buffer = new StringBuffer();
        Cursor res = DB.getLatestUser(currentUser.getId());

//        res.getString(res.getColumnIndex("user_name"));
        while (res.moveToNext()) {
            String name = buffer.append(
                    res.getString(res.getColumnIndex("user_name"))
            ) + "\n";
            buffer.delete(0, res.getString(res.getColumnIndex("user_name")).length());
            StringBuffer password = buffer.append(
                    res.getString(res.getColumnIndex("user_password"))
            );

            textInputEditTextName.setText(name);
//            textInputEditTextEmail.setText(email);
            textInputEditTextPassword.setText(password.toString());
        }
    }


    private void initListeners() {
        btn_save_profile.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
//        databaseHelper = new DBHelper(activity);
//        user = new User();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_save_profile:
                postDataToSQLite();

                break;

        }
    }

    private void postDataToSQLite() {

        //prevent empty
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        DB.open();

        String name = textInputEditTextName.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();
        //replace the space entered
        currentUser.setUsername(name.replace(" ", ""));
        Log.d(TAG, name); //text show in console to double check

        currentUser.setPassword(password);
        Log.d(TAG, password); //text show in console to double check
        Log.d(TAG, "id: " + currentUser.getId()); //text show in console to double check
        DB.updateUser(currentUser);
        // Snack Bar to show success message that record saved successfully
        Snackbar.make(constraintLayout, getString(R.string.success_update), Snackbar.LENGTH_LONG).show();
//            emptyInputEditText();
        fragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", currentUser);//pass the value user
        fragment.setArguments(bundle);
//        loadFragment(fragment);
        finish();
    }

}