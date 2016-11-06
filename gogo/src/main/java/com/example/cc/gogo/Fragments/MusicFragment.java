package com.example.cc.gogo.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cc.gogo.R;

import co.mobiwise.library.InteractivePlayerView;

/**
 * Created by spencer on 2016/11/6.
 */

public class MusicFragment extends Fragment implements View.OnClickListener{

    View view;
    TextView view1,view2,view3;

    public MusicFragment()
    {}

    @Override
    public void onClick(View v) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_music, container, false);
        init();
        final InteractivePlayerView ipv = (InteractivePlayerView) view.findViewById(R.id.ipv);
        ipv.setMax(123);
        ipv.setProgress(78);
        //ipv.setOnActionClickedListener(this);


        final ImageView control = (ImageView)view.findViewById(R.id.control);
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

        final ImageView bottom = (ImageView)view.findViewById(R.id.imageBottom);
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

        view1 = (TextView) view.findViewById(R.id.singerText);
        view2 = (TextView) view.findViewById(R.id.songText);
        view3 = (TextView) view.findViewById(R.id.listheader);
        //view4 = (TextView) view.findViewById(R.id.step_percent);


        return view;
    }

    private void init() {
        findView();
        setOnClickListener();
        setOnItemSelectListener();
    }

    /**
     * 查找控件
     */
    protected void findView()
    {

    }

    /**
     * 设置点击事件
     */
    protected void setOnClickListener()
    {}

    /**
     * 设置itemSelectListener
     */
    protected void setOnItemSelectListener()
    {}
}
