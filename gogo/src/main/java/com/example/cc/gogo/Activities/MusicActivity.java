package com.example.cc.gogo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cc.gogo.R;

import co.mobiwise.library.InteractivePlayerView;
import co.mobiwise.library.OnActionClickedListener;

public class MusicActivity extends AppCompatActivity implements OnActionClickedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);


        final InteractivePlayerView ipv = (InteractivePlayerView) findViewById(R.id.ipv);
        ipv.setMax(123);
        ipv.setProgress(78);
        ipv.setOnActionClickedListener(this);


        final ImageView control = (ImageView) findViewById(R.id.control);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ipv.isPlaying()){
                    ipv.start();
                    control.setBackgroundResource(R.drawable.pause);
                }
                else{
                    ipv.stop();
                    control.setBackgroundResource(R.drawable.play);
                }
            }
        });
    }

    @Override
    public void onActionClicked(int id) {
        switch (id){
            case 1:
                //Called when 1. action is clicked.
                break;
            case 2:
                //Called when 2. action is clicked.
                break;
            case 3:
                //Called when 3. action is clicked.
                break;
            default:
                break;
        }
    }
}
