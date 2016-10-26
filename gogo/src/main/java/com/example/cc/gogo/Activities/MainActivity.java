package com.example.cc.gogo.Activities;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;

import java.util.ArrayList;

import android.support.annotation.*;
import android.util.Log;

import com.example.cc.gogo.Fragments.CollectionFragment;
import com.example.cc.gogo.Fragments.RunFragment;
import com.example.cc.gogo.Fragments.SettingFragment;
import com.example.cc.gogo.R;
import com.roughike.bottombar.*;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.cc.gogo.R.layout.main_activity);
        //initial fragment show
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CollectionFragment cf = new CollectionFragment();
        //fragmentTransaction.replace(R.id.contentContainer,cf);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();
        //bottom show
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (tabId) {
                    case R.id.home:
                        Log.i("info", "click");
                        CollectionFragment cf = new CollectionFragment();
                        //fragmentTransaction.replace(R.id.contentContainer,cf);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.run:
                        RunFragment rf = new RunFragment();
                        fragmentTransaction.replace(R.id.contentContainer, rf);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.setting:
                        SettingFragment sf = new SettingFragment();
                        fragmentTransaction.replace(R.id.contentContainer,sf);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}