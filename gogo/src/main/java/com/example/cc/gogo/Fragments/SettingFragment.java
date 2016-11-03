package com.example.cc.gogo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cc.gogo.Activities.CollectionMenuActivity;
import com.example.cc.gogo.Activities.MyAimActivity;
import com.example.cc.gogo.Activities.SettingsActivity;
import com.example.cc.gogo.Activities.StepsActivity;
import com.example.cc.gogo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    Button collectButton;
    Button settingButton;
    Button my_aimButton;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        findView(rootView);
        setOnclickListener();
        return rootView;
    }

    public void findView(View rootView) {
        collectButton = (Button) rootView.findViewById(R.id.collect);
        settingButton = (Button) rootView.findViewById(R.id.setting);
        my_aimButton=(Button)rootView.findViewById(R.id.my_aim);
    }

    public void setOnclickListener() {
        collectButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        my_aimButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect:
                startActivity(new Intent(getActivity(), CollectionMenuActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.my_aim:
                startActivity(new Intent(getActivity(),MyAimActivity.class));
                break;
            default:
                break;
        }
    }
}
