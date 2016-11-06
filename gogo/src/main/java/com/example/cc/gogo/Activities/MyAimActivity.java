package com.example.cc.gogo.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.cc.gogo.LinearRegression.LinearRegressionPredict;
import com.example.cc.gogo.R;
import com.example.cc.gogo.util.StepDetector;
import com.lovearthstudio.duasdk.util.SharedPreferenceUtil;

import butterknife.OnClick;

public class MyAimActivity extends AppCompatActivity implements View.OnClickListener {
    Button use_advice, set_by_self;
    TextView advice_step, target_step;
    int step_length;
    int step;
    double target;
    public static final String PREF_PROFILE = "profile";
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
        init();
    }

    public void init() {
        String age = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "age", "");
        String height = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "height", "");
        String weight = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "weight", "");
        String frequency = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "frequency", "");
        if (!(age.equals("") | height.equals("") | weight.equals("") | frequency.equals(""))) {
            //设置目标步数
            if (SettingsActivity.sharedPreferences != null) {
                step_length = SettingsActivity.sharedPreferences.getInt(
                        SettingsActivity.STEP_LENGTH_VALUE, 70);
            } else {
                step_length = 50;
            }
            if (SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", "").equals("")) {
                double[] elements = new double[]{Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency)};
                try {
                    double advice_target = LinearRegressionPredict.calc_mile(elements);
                    double steps = advice_target * 1000 * 100 / step_length / 3 * 2;
                    step = (int) steps;
                    SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(advice_target));
                    SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                    target_step.setText(step + "步");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                target = Double.parseDouble(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", ""));
                double steps = target * 1000 * 100 / step_length / 3 * 2;
                step = (int) steps;
                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(target));
                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                target_step.setText(step + "步");
            }
            //设置系统推荐步数
            if (!(age.equals("") | height.equals("") | weight.equals("") | frequency.equals(""))) {
                try {
                    double[] elements = new double[]{Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency)};
                    double advice_target = LinearRegressionPredict.calc_mile(elements);
                    double steps = advice_target * 1000 * 100 / step_length / 3 * 2;
                    step = (int) steps;
                    advice_step.setText(step + "步");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            advice_step.setText(10000 + "步");
            target_step.setText(10000 + "步");
        }
    }

    public void getView() {
        use_advice = (Button) findViewById(R.id.use_advice);
        set_by_self = (Button) findViewById(R.id.set_by_self);
        advice_step = (TextView) findViewById(R.id.advice_step);
        target_step = (TextView) findViewById(R.id.target_step);
    }

    public void setOnClickListener() {
        use_advice.setOnClickListener(this);
        set_by_self.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MyAimActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.set_by_self:
                builder = new AlertDialog.Builder(MyAimActivity.this);
                builder.setTitle("选择强度");
                builder.setItems(new String[]{"大量增加", "增加", "不变", "减少"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double steps;
                        switch (which) {
                            case 0:
                                target = 1.4 * Double.parseDouble(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", ""));
                                steps = target * 1000 * 100 / step_length / 3 * 2;
                                step = (int) steps;
                                target_step.setText(step + "步");
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(target));
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                                break;
                            case 1:
                                target = 1.2 * Double.parseDouble(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", ""));
                                steps = target * 1000 * 100 / step_length / 3 * 2;
                                step = (int) steps;
                                target_step.setText(step + "步");
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(target));
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                                break;
                            case 2:
                                target = Double.parseDouble(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", ""));
                                steps = target * 1000 * 100 / step_length / 3 * 2;
                                step = (int) steps;
                                target_step.setText(step + "步");
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(target));
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                                break;
                            case 3:
                                target = 0.8 * Double.parseDouble(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", ""));
                                steps = target * 1000 * 100 / step_length / 3 * 2;
                                step = (int) steps;
                                target_step.setText(step + "步");
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "target", String.valueOf(target));
                                SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                break;
            case R.id.use_advice:
                String age = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "age", "");
                String height = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "height", "");
                String weight = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "weight", "");
                String frequency = SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "frequency", "");

                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "age", ""));
                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "height", ""));
                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "weight", ""));
                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "frequency", ""));

                if (!(age.equals("") | height.equals("") | weight.equals("") | frequency.equals(""))) {
                    try {
                        double[] elements = new double[]{Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency)};
                        //double target = LinearRegressionPredict.calc_mile(Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency));
                        target = LinearRegressionPredict.calc_mile(elements);
                        Log.i("target_result", target + "");
                        SharedPreferenceUtil.prefSetKey(this, PREF_PROFILE, "target", String.valueOf(target));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                builder = new AlertDialog.Builder(MyAimActivity.this);
                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "target", ""));
                if (SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "target", "").equals("")) {
                    builder.setTitle("提示");
                    builder.setMessage("请完善个人信息");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                } else {
                    if (SettingsActivity.sharedPreferences != null) {
                        step_length = SettingsActivity.sharedPreferences.getInt(
                                SettingsActivity.STEP_LENGTH_VALUE, 70);
                    } else {
                        step_length = 50;
                    }
                    Log.i("steps", target + "");
                    double steps = target * 1000 * 100 / step_length / 3 * 2;
                    step = (int) steps;
                    SharedPreferenceUtil.prefSetKey(getApplicationContext(), PREF_PROFILE, "step", String.valueOf(step));
                    Log.i("steps", steps + "");
                    builder.setTitle("确认");
                    builder.setMessage("已使用推荐设置！");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定按钮的点击事件
                            advice_step.setText(step + "步");
                            target_step.setText(step + "步");
                        }
                    });
                    builder.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        init();
    }
}
