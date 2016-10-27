package com.example.cc.gogo.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cc.gogo.Fragments.base.BaseFragment;
import com.example.cc.gogo.R;

public class CollectionFragment extends BaseFragment {


    public CollectionFragment() {
        // Required empty public constructor
    }

    public static CollectionFragment newInstance() {

        Bundle args = new Bundle();

        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect_data, container, false);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void setOnClickListener() {

    }

    @Override
    protected void setOnItemSelectListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
