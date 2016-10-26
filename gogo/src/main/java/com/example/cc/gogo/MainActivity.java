package com.example.cc.gogo;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;
import java.util.ArrayList;
import android.support.annotation.*;
import android.util.Log;

import com.roughike.bottombar.*;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //initial fragment show
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CollectionFragment cf = new CollectionFragment();
     //   fragmentTransaction.replace(R.id.contentContainer,cf);
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
                switch (tabId)
                {
                    case R.id.home:
                        Log.i("info","click");
                        CollectionFragment cf = new CollectionFragment();
               //         fragmentTransaction.replace(R.id.contentContainer,cf);
                        fragmentTransaction.addToBackStack("");
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.run:
                        Log.i("info","click");
                        RunFragment rf = new RunFragment();
                        fragmentTransaction.replace(R.id.contentContainer,rf);
                        fragmentTransaction.addToBackStack("");
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