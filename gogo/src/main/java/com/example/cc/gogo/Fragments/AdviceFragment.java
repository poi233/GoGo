package com.example.cc.gogo.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cc.gogo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceFragment extends Fragment {


    public AdviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_advice, container, false);

    }

}
