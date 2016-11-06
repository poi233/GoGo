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
import com.lovearthstudio.duasdk.util.SharedPreferenceUtil;

import butterknife.OnClick;

public class MyAimActivity extends AppCompatActivity implements View.OnClickListener {
    Button use_advice, set_by_self;
    TextView advice_step;
    public static final String PREF_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
    }

    public void getView() {
        use_advice = (Button) findViewById(R.id.use_advice);
        set_by_self = (Button) findViewById(R.id.set_by_self);
        advice_step = (TextView) findViewById(R.id.advice_step);
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
                builder.setItems(new String[]{"很高", "高", "中", "低"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("which", which + "");
                    }
                });
                builder.show();
                builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("builder", position + "");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
                        Log.i("target_result", "in");
                        double[] elements = new double[]{Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency)};
                        //double target = LinearRegressionPredict.calc_mile(Double.parseDouble(age), Double.parseDouble(height), Double.parseDouble(weight), Double.parseDouble(frequency));
                        double target = LinearRegressionPredict.calc_mile(elements);
                        Log.i("target_result", target + "");
                        SharedPreferenceUtil.prefSetKey(this, PREF_PROFILE, "target", String.valueOf(target));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                builder = new AlertDialog.Builder(MyAimActivity.this);
                Log.i("target", SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "target", ""));
                if (SharedPreferenceUtil.prefGetKey(this, PREF_PROFILE, "target", "") == null) {
                    builder.setTitle("提示");
                    builder.setMessage("请完善个人信息");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                } else {
                    builder.setTitle("确认");
                    builder.setMessage("已使用推荐设置！");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定按钮的点击事件
                            advice_step.setText(SharedPreferenceUtil.prefGetKey(getApplicationContext(), PREF_PROFILE, "target", "") + "公里");
                        }
                    });
                    builder.show();
                }
                break;
            default:
                break;

        }
    }
}
