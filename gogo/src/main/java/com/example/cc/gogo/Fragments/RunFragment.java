package com.example.cc.gogo.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import static com.example.cc.gogo.svm.SVM.dataToFeaturesArr;
import static com.example.cc.gogo.svm.SVM.inputStreamToArray;
import static com.example.cc.gogo.util.Constant.dir;
import static com.example.cc.gogo.util.Constant.actMapFromCode;
import static com.example.cc.gogo.util.Constant.wzMapFromCode;
import static com.example.cc.gogo.util.Constant.rangeFileName;
import static com.example.cc.gogo.util.Constant.modelFileName;

import static java.io.File.separator;

import com.example.cc.gogo.svm.*;
import com.example.cc.gogo.Activities.MainActivity;
import com.example.cc.gogo.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import libsvm.svm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment implements View.OnClickListener {
    View view;
    MainActivity mActivity;
    Button btn_reset,btn_start;
    TextView tv_run,tv_walk;
    Chronometer cm_pass_time;
    SensorManager sensorManager;
    MySensorListener sensorListener;

    public RunFragment() {
        // Required empty public constructor
    }

    protected void findView() {
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        tv_run = (TextView) view.findViewById(R.id.tv_run);
        tv_walk = (TextView) view.findViewById(R.id.tv_walk);
        cm_pass_time = (Chronometer) view.findViewById(R.id.cm_pass_time);
    }

    protected void setOnClickListener() {
        btn_reset.setOnClickListener(this);
        btn_start.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_run, container, false);
        findView();
        setOnClickListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (MainActivity) getActivity();
        sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_start:
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        1000 * 1000 / 32);
                loadModelAndRange();
                break;
            case R.id.btn_reset:
                break;
        }
    }

    SVM mSvm;

    /**
     * 加载model和range
     */
    private void loadModelAndRange() {
        try {
            mSvm = new SVM(svm.svm_load_model(
                    new BufferedReader(new InputStreamReader(new FileInputStream(dir + separator + modelFileName)))),
                    inputStreamToArray(new FileInputStream(dir + separator + rangeFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MySensorListener implements SensorEventListener {
        int num = 128;
        public double[] accArr = new double[num];
        public int currentIndex = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            /**
             * 采集128个数据,归一化,预测
             */
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double a = Math.sqrt((double) (x * x + y * y + z * z));
                Log.i("info",String.valueOf(a));
                //mTvSensor.setText("加速度:" + a);
                if (currentIndex >= num) {
                    String[] data = dataToFeaturesArr(accArr.clone());
                    Log.i("info",data[1]);
                    double code = mSvm.predictUnscaled(data, false);
                    double act = (int) code / 100;
                    double position = code - act * 100;
                    String strAct = actMapFromCode.get(act);
                    String strPosition = wzMapFromCode.get(position);
                    System.out.println("--------" + code + ":" + act + ":" + position);
                    if (strAct.equals("Walking")) {
                        tv_walk.setTextColor(230);
                    }
                    //mTvResult.setText("预测:action" + strAct + "------postion:" + strPosition);
                    currentIndex = 0;
                }
                accArr[currentIndex++] = a;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

}
