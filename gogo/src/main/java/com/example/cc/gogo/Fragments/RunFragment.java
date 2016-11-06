package com.example.cc.gogo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
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

import com.example.cc.gogo.Activities.SettingsActivity;
import com.example.cc.gogo.Service.StepCounterService;
import com.example.cc.gogo.svm.*;
import com.example.cc.gogo.Activities.MainActivity;
import com.example.cc.gogo.R;
import com.example.cc.gogo.util.StepDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.SynchronousQueue;

import libsvm.svm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment implements View.OnClickListener {
    View view;
    MainActivity mActivity;
    Button btn_reset, btn_start, btn_pause;
    TextView tv_run, tv_walk;
    TextView cm_pass_time;
    SensorManager sensorManager;
    StepDetector sensorListener;
    SVM mSvm;
    TextView tv_distance, tv_calories, tv_velocity;
    //running parameters
    private long timer = 0;// 运动时间
    private long startTimer = 0;// 开始时间

    private long tempTime = 0;

    private Double distance = 0.0;// 路程：米
    private Double walk_disance = 0.0;
    private Double run_disatance = 0.0;
    private Double calories = 0.0;// 热量：卡路里
    private Double velocity = 0.0;// 速度：米每秒

    private int step_length = 0;  //步长
    private int weight = 0;       //体重
    private int total_step = 0;   //走的总步数
    private int run_step = 0;
    private int walk_step = 0;
    private int moving_status = 0;

    private Thread thread;  //定义线程对象


    Handler handler =  new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
        //主要接受子线程发送的数据, 并用此数据配合主线程更新UI
        //Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据,
        //Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
        //把这些消息放入主线程队列中，配合主线程进行更新UI。

        @Override                  //这个方法是从父类/接口 继承过来的，需要重写一次
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);        // 此处可以更新UI

            countDistance();     //调用距离方法，看一下走了多远

            if (timer != 0 && distance != 0.0) {

                // 体重、距离
                // 跑步热量（kcal）＝ 体重（kg）×距离（公里）×1.036
                // 走路热量 (kcal) = 体重（kg）×距离（公里）×0.259
                Double run_calories = weight * distance * 0.001036;
                Double walk_calories = weight * distance * 0.000259;
                calories = run_calories + walk_calories;

                //速度velocity
                velocity = distance * 1000 / timer;
            } else {
                calories = 0.0;
                velocity = 0.0;
            }

            countStep();          //调用步数方法
            getMovingStatus();    //获取运动状态
            if (moving_status == 1) {
                tv_walk.setText(walk_step + "");
                tv_walk.setTextColor(Color.parseColor("#ffff4444"));
                tv_run.setTextColor(Color.parseColor("#ff33b5e5"));
            } else if (moving_status == 2) {
                tv_run.setText(run_step + "");// 显示当前步数
                tv_run.setTextColor(Color.parseColor("#ffff4444"));
                tv_walk.setTextColor(Color.parseColor("#ff33b5e5"));
            } else {
                tv_walk.setTextColor(Color.parseColor("#ff33b5e5"));
                tv_run.setTextColor(Color.parseColor("#ff33b5e5"));
            }

            tv_distance.setText(formatDouble(distance));// 显示路程
            tv_calories.setText(formatDouble(calories));// 显示卡路里
            tv_velocity.setText(formatDouble(velocity));// 显示速度

            cm_pass_time.setText(getFormatTime(timer));// 显示当前运行时间


        }
    };


    public RunFragment() {
        // Required empty public constructor
    }

    protected void findView() {
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_pause = (Button) view.findViewById(R.id.btn_pause);
        tv_run = (TextView) view.findViewById(R.id.tv_run);
        tv_walk = (TextView) view.findViewById(R.id.tv_walk);
        cm_pass_time = (TextView) view.findViewById(R.id.cm_pass_time);
        tv_calories = (TextView) view.findViewById(R.id.tv_calorie);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_velocity = (TextView) view.findViewById(R.id.tv_velocity);
    }

    protected void setOnClickListener() {
        btn_reset.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_run, container, false);
        findView();
        setOnClickListener();
        init();
        mThread();
        return view;
    }

    public void mThread() {
        if (thread == null) {
            thread = new Thread() {// 子线程用于监听当前步数的变化

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int temp = 0;
                    while (true) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (StepCounterService.FLAG) {
                            Message msg = new Message();
                            total_step = StepDetector.RUN_SETP + StepDetector.WALK_STEP;
                            if (temp != total_step) {
                                temp = total_step;
                            }
                            if (startTimer != System.currentTimeMillis()) {
                                timer = System.currentTimeMillis()
                                        - startTimer;
                            }
                            //Log.i("time", "startTimer=" + getFormatTime(startTimer) + " timer=" + getFormatTime(timer) + " tempTimer=" + getFormatTime(tempTime));
                            handler.sendMessage(msg);// 通知主线程
                        }
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (MainActivity) getActivity();
        sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new StepDetector(this.getActivity());
    }

    /**
     * 计算并格式化doubles数值，保留两位有效数字
     *
     * @param doubles
     * @return 返回当前路程
     */
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals(getString(R.string.zero)) ? getString(R.string.double_zero)
                : distanceStr;
    }

    @Override
    public void onClick(View v) {
        Intent service = new Intent(getActivity(), StepCounterService.class);
        switch (v.getId()) {
            case R.id.btn_start:
                getActivity().startService(service);
                startTimer = System.currentTimeMillis() - tempTime;
                btn_start.setVisibility(View.GONE);
                btn_pause.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pause:
                getActivity().stopService(service);
                tv_walk.setTextColor(Color.parseColor("#ff33b5e5"));
                tv_run.setTextColor(Color.parseColor("#ff33b5e5"));
                tempTime = System.currentTimeMillis()-startTimer;
                btn_start.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                break;
            case R.id.btn_reset:
                getActivity().stopService(service);
                StepDetector.CURRENT_SETP = 0;
                StepDetector.WALK_STEP = 0;
                StepDetector.RUN_SETP = 0;
                StepDetector.MOTIVATION_STATUS = 0;
                tv_walk.setTextColor(Color.parseColor("#ff33b5e5"));
                tv_run.setTextColor(Color.parseColor("#ff33b5e5"));
                //tempTime =
                distance = calories = velocity = 0.0;
                timer = tempTime = 0;
                init();
                btn_start.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                tv_distance.setText(formatDouble(0.0));
                tv_calories.setText(formatDouble(0.0));
                tv_velocity.setText(formatDouble(0.0));
                cm_pass_time.setText(getFormatTime(timer));
                tv_walk.setText(0 + "");
                tv_run.setText(0 + "");
                handler.removeCallbacks(thread);
                break;
        }
    }

    private String getFormatTime(long time) {
        time = time / 1000;
        long second = time % 60;
        long minute = (time % 3600) / 60;
        long hour = time / 3600;

        // 毫秒秒显示两位
        // String strMillisecond = "" + (millisecond / 10);
        // 秒显示两位
        String strSecond = ("00" + second)
                .substring(("00" + second).length() - 2);
        // 分显示两位
        String strMinute = ("00" + minute)
                .substring(("00" + minute).length() - 2);
        // 时显示两位
        String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

        return strHour + ":" + strMinute + ":" + strSecond;
        // + strMillisecond;
    }


    /**
     * 计算行走的距离
     */
    private void countDistance() {
        if ((StepDetector.WALK_STEP + StepDetector.RUN_SETP) == 0) {
            distance = ((StepDetector.WALK_STEP + StepDetector.RUN_SETP) / 2) * 3 * step_length * 0.01;
        } else {
            distance = ((StepDetector.WALK_STEP + StepDetector.RUN_SETP) / 2 * 3 + 1) * step_length * 0.01;
        }
        if ((StepDetector.RUN_SETP) == 0) {
            run_disatance = ((StepDetector.RUN_SETP) / 2) * 3 * step_length * 0.01;
        } else {
            run_disatance = ((StepDetector.RUN_SETP) / 2 * 3 + 1) * step_length * 0.01;
        }
        if ((StepDetector.WALK_STEP) == 0) {
            walk_disance = ((StepDetector.WALK_STEP) / 2) * 3 * step_length * 0.01;
        } else {
            walk_disance = ((StepDetector.WALK_STEP) / 2 * 3 + 1) * step_length * 0.01;
        }
    }

    /**
     * 实际的步数
     */
    private void countStep() {
        if ((StepDetector.WALK_STEP + StepDetector.RUN_SETP) % 2 == 0) {
            total_step = (StepDetector.WALK_STEP + StepDetector.RUN_SETP);
        } else {
            total_step = (StepDetector.WALK_STEP + StepDetector.RUN_SETP) + 1;
        }

        if (StepDetector.RUN_SETP % 2 == 0) {
            run_step = StepDetector.RUN_SETP;
        } else {
            run_step = StepDetector.RUN_SETP + 1;
        }

        if (StepDetector.WALK_STEP % 2 == 0) {
            walk_step = StepDetector.WALK_STEP;
        } else {
            walk_step = StepDetector.WALK_STEP + 1;
        }
        /*walk_step = StepDetector.WALK_STEP;
        run_step = StepDetector.RUN_SETP;
        total_step = StepDetector.CURRENT_SETP;*/
    }

    public void getMovingStatus() {
        moving_status = StepDetector.MOTIVATION_STATUS;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i("APP", "on resuame.");
        tv_run.setText(run_step + "");
        tv_walk.setText(walk_step + "");
        tv_distance.setText(formatDouble(distance));// 显示路程
        tv_calories.setText(formatDouble(calories));// 显示卡路里
        tv_velocity.setText(formatDouble(velocity));// 显示速度
        //timer = System.currentTimeMillis()-startTimer;
        //cm_pass_time.setText(getFormatTime(timer));
        // 获取界面控件
        /*addView();*/
        /*// 初始化控件*/
        /*init();*/
    }

    /**
     * 获取Activity相关控件
     */
    private void addView() {
        findView();
        Intent service = new Intent(getActivity(), StepCounterService.class);
        getActivity().stopService(service);
        StepDetector.CURRENT_SETP = 0;
        tempTime = timer = 0;
        cm_pass_time.setText(getFormatTime(timer));      //如果关闭之后，格式化时间
        /*tv_show_step.setText("0");
        tv_distance.setText(formatDouble(0.0));
        tv_calories.setText(formatDouble(0.0));
        tv_velocity.setText(formatDouble(0.0));*/
        handler.removeCallbacks(thread);
    }


    /**
     * 初始化界面
     */
    private void init() {
        if (SettingsActivity.sharedPreferences != null) {
            step_length = SettingsActivity.sharedPreferences.getInt(
                    SettingsActivity.STEP_LENGTH_VALUE, 70);
            weight = SettingsActivity.sharedPreferences.getInt(
                    SettingsActivity.WEIGHT_VALUE, 50);
        } else {
            step_length = 70;
            weight = 50;
        }

        countDistance();
        countStep();
        //Log.i("time", getFormatTime(timer) + " " + getFormatTime(tempTime));
        if ((timer) != 0 && distance != 0.0) {  //tempTime记录运动的总时间，timer记录每次运动时间

            // 体重、距离
            // 跑步热量（kcal）＝ 体重（kg）×距离（公里）×1.036
            // 走路热量 (kcal) = 体重（kg）×距离（公里）×0.259
            Double run_calories = weight * distance * 0.001036;
            Double walk_calories = weight * distance * 0.000259;
            calories = run_calories + walk_calories;
            velocity = distance * 1000 / timer;
        } else {
            calories = 0.0;
            velocity = 0.0;
        }
        cm_pass_time.setText(getFormatTime(timer));
        tv_run.setText(0 + "");
        tv_walk.setText(0 + "");
        tv_distance.setText(formatDouble(distance));
        tv_calories.setText(formatDouble(calories));
        tv_velocity.setText(formatDouble(velocity));

        //tv_show_step.setText(total_step + "");
        //btn_start.setEnabled(!StepCounterService.FLAG);
        //btn_pause.setEnabled(StepCounterService.FLAG);*/

        /*if (StepCounterService.FLAG) {
            btn_pause.setText(getString(R.string.pause));
        } else if (StepDetector.CURRENT_SETP > 0) {
            btn_pause.setEnabled(true);
            btn_pause.setText(getString(R.string.cancel));
        }*/
    }

}