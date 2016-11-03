package com.example.cc.gogo.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cc.gogo.R;
import com.example.cc.gogo.Service.StepCounterService;
import com.example.cc.gogo.util.StepDetector;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceFragment extends Fragment {
    View view;
    TextView tv_step_num, tv_step_target, tv_step_percent;
    private Double calories = 0.0;// 热量：卡路里
    private Double velocity = 0.0;// 速度：米每秒
    private int total_step = 0;   //走的总步数
    private Double distance = 0.0;// 路程：米
    private int step_length = 0;  //步长
    private Thread thread;  //定义线程对象
    private Double target = 40.0;


    Handler handler = new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
        //主要接受子线程发送的数据, 并用此数据配合主线程更新UI
        //Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据,
        //Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
        //把这些消息放入主线程队列中，配合主线程进行更新UI。

        @Override                  //这个方法是从父类/接口 继承过来的，需要重写一次
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);        // 此处可以更新UI
            total_step = StepDetector.RUN_SETP + StepDetector.WALK_STEP;
            double percent = total_step / target > 1 ? 100 : total_step / target * 100.0;
            int per = (int) percent;
            tv_step_num.setText(String.valueOf(total_step));
            tv_step_percent.setText(String.valueOf(per)+"%");
        }
    };

    public AdviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advice, container, false);
        initView();
        mThread();
        return view;
    }

    public void initView() {
        tv_step_num = (TextView) view.findViewById(R.id.step_num);
        tv_step_target = (TextView) view.findViewById(R.id.step_target);
        tv_step_percent = (TextView) view.findViewById(R.id.step_percent);
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
                            if (temp != (StepDetector.WALK_STEP + StepDetector.RUN_SETP)) {
                                temp = (StepDetector.WALK_STEP + StepDetector.RUN_SETP);
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
    public void onResume()
    {
        super.onResume();
        total_step = StepDetector.RUN_SETP + StepDetector.WALK_STEP;
        double percent = total_step / target > 1 ? 100 : total_step / target * 100.0;
        int per = (int) percent;
        tv_step_num.setText(String.valueOf(total_step));
        tv_step_percent.setText(String.valueOf(per)+"%");
    }


}
