package com.digitallibrary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private User currentUser;
    CardView btn_find_books, btn_malaysia_library, btn_audio_book;
    private static final String TAG = HomeFragment.class.getSimpleName();
    VideoView videoView;
    private boolean mVolumePlaying = false;
    AppCompatImageView volume;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        currentUser = (User) bundle.getSerializable("user");

        btn_find_books = v.findViewById(R.id.btn_search_book);
        btn_malaysia_library = v.findViewById(R.id.btn_library);
        btn_audio_book = v.findViewById(R.id.btn_audio_list);

        volume = v.findViewById(R.id.volume);

        //set the video
        videoView = v.findViewById(R.id.recommendation);
        //set the video path that in the raw folder
        String videoPath =
                "android.resource://" +
                        requireActivity().getPackageName() +
                        "/" +
                        R.raw.summer_book_recommendations;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        //Mediacontroller contains the buttons like "Play/Pause"that we need
        MediaController mediaController = new MediaController(getActivity());
        //set the video to the media controller
        videoView.setMediaController(mediaController);
        //set the video to start automatically
        videoView.start();
        //set the video a callback to be invoked, take a listener as parameter.
        videoView.setOnPreparedListener(this::PreparedListener);

        btn_find_books.setOnClickListener(this);
        btn_malaysia_library.setOnClickListener(this);
        btn_audio_book.setOnClickListener(this);

        return v;
    }

    private void PreparedListener(MediaPlayer mp) {
        try {
            //mute the volume of the video when the user enter the page
            mp.setVolume(0f, 0f);
            //if mute volume is true, the mute icon is shown
            if (!mVolumePlaying) {
                volume.setImageResource(R.drawable.ic_volume_off);
            }

            //set the looping of video on
            mp.setLooping(true);
            mp.start();

            //user can click on the image to change the volume (mute or unmute)
            volume.setOnClickListener(v -> {

                //if mute is false, and the icon is clicked
                // means the user want to mute the volume, so mute icon is shown
                if (mVolumePlaying) {
                    Log.d(TAG, "setVolume OFF"); //text show in console to double check
                    volume.setImageResource(R.drawable.ic_volume_off);
                    mp.setVolume(0F, 0F);
                }
                //else if mute is true, and the icon is clicked
                // means the user want to unmute the volume, so unmute icon is shown
                else {
                    Log.d(TAG, "setVolume ON"); //text show in console to double check
                    volume.setImageResource(R.drawable.ic_volume_up);
                    mp.setVolume(1F, 1F);
                }
                //when the clicking is finished, set the mVolumePlaying to opposite (due to the if else above)
                //so the the difference can be observed the click is functional
                mVolumePlaying = !mVolumePlaying;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn_search_book:
                intent = new Intent(getActivity(), FindBooks.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("Username", currentUser.getUsername());
                startActivity(intent);
                break;
            case R.id.btn_library:
                intent = new Intent(getActivity(), MalaysiaLibrary.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("Username", currentUser.getUsername());
                startActivity(intent);
                break;
            case R.id.btn_audio_list:
                intent = new Intent(getActivity(), SelectAudioActivity.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("Username", currentUser.getUsername());
                startActivity(intent);
                break;

        }
    }
}