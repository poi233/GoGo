package com.example.cc.gogo.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cc.gogo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.cc.gogo.svm.SVM.dataToFeaturesArr;
import static com.example.cc.gogo.util.Constant.dir;
import static com.example.cc.gogo.util.Constant.train;

public class LoadingActivity extends AppCompatActivity {
    com.shinelw.library.ColorArcProgressBar progressBar;
    String step, state;//上级传参
    public static SharedPreferences sharedPreferences;
    String data[][] = new String[25][10];

    int trainNum;

    double mAtionInt = 1;                               // action 的label
    double mPostionInt = 1;                             // position 的label
    int mSensorHzInt;                                   // sensor采样频率

    Boolean isFinish = true;
    SensorManager sensorManager;                        // 传感器管理器
    MySensorListener sensorListener;                    // 传感器监听类,当传感器数据变化时会调用该类的onSensorChanged()方法
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {
        progressBar = (com.shinelw.library.ColorArcProgressBar) findViewById(R.id.progressBar);
        progressBar.setCurrentValues(0);
        Intent intent = this.getIntent();
        step = intent.getStringExtra("step");
        state = intent.getStringExtra("state");
        //Log.i("info", "step=" + step + " state=" + state);
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();

        try {
            outputStream = new FileOutputStream(dir + File.separator + "train_self.txt", true);
            Log.i("fileCreate", "outputStream:" + dir + File.separator + "train_self.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        openSensor();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoadingActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 关闭传感器
     */
    private void closeSensor() {
        sensorManager.unregisterListener(sensorListener);
    }

    /**
     * 根据采样频率打开传感器
     */
    private void openSensor() {
        /*sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                1000 * 1000 / 32);*/
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
    }

    public class MySensorListener implements SensorEventListener {
        int num = 128;
        public double[] accArr = new double[num];
        public int currentIndex = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            /**
             * 采集128个数据,转换成特征值
             */
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double a = Math.sqrt((double) (x * x + y * y + z * z));
                //Log.i("info", "lable:" + (mAtionInt * 100 + mPostionInt) + "加速度:" + a);
                //Log.i("info", "采集样本数量:" + trainNum);
                if (currentIndex >= num) {
                    data[trainNum] = dataToFeaturesArr(accArr.clone());
                    currentIndex = 0;
                    trainNum++;
                    progressBar.setCurrentValues(trainNum * 4);
                    if (trainNum == 25) {
                        closeSensor();
                        saveDataToFile(data);
                        trainNum = 0;
                        if (sharedPreferences == null) {    //SharedPreferences是Android平台上一个轻量级的存储类，
                            //主要是保存一些常用的配置比如窗口状态
                            sharedPreferences = getSharedPreferences("CollectStatus", MODE_PRIVATE);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(state+step,"clear");
                        Log.i("shared",state+step);
                        editor.putString(state, "clear");
                        //Log.i("status", state + " clear");
                        editor.commit();
                        Toast.makeText(LoadingActivity.this, "数据采集完成！", Toast.LENGTH_LONG).show();
                    }
                }
                accArr[currentIndex++] = a;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    /**
     * 把数据保存到文件中
     *
     * @param data
     */
    private void saveDataToFile(String[][] data) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n");
        Log.i("data",data[0][1]+" "+data[0][2]);
        for (int i = 5; i < 20; i++) {
            stringBuffer.append(mAtionInt * 100 + mPostionInt);
            for (int j = 0;j<8;j++) {
                stringBuffer.append(" " + data[i][j]);
            }
            stringBuffer.append("\n");
        }
        try {
            outputStream.write(stringBuffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        closeSensor();
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }
}
