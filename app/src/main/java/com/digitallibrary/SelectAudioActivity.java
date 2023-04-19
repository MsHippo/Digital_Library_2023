package com.digitallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.digitallibrary.databases.DBAccess;

public class SelectAudioActivity extends AppCompatActivity {

    CardView btn_audio_book1;
    private User currentUser;
    DBAccess DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_audio);

        btn_audio_book1 = findViewById(R.id.btn_audio_book1);
        currentUser = (User) getIntent().getSerializableExtra("user");

        btn_audio_book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAudioActivity.this, AudioActivity.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("Username", currentUser.getUsername());
                startActivity(intent);
                finish();
            }
        });

    }
}