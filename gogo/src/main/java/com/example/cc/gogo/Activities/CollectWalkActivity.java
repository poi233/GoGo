package com.example.cc.gogo.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dd.CircularProgressButton;
import com.example.cc.gogo.R;
import com.example.cc.gogo.Service.StepCounterService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.cc.gogo.util.Constant.dir;

import android.app.AlertDialog;

public class CollectWalkActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup step_group;
    RadioButton hand, pocket, arm;//获取被选取的RadioButton
    public static SharedPreferences sharedPreferences;
    public String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_walk);//获取界面组件
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
        init();
    }

    private void init() {
        Intent intent = this.getIntent();
        state = intent.getStringExtra("state");
        sharedPreferences = getSharedPreferences("CollectStatus", MODE_PRIVATE);
        Log.i("shared", sharedPreferences.getString(state + "Hand Fixed", ""));
        if (sharedPreferences.getString(state + "Hand Fixed", "").equals("clear")) {
            Log.i("shared", "手持模式clear");
            hand.setText("手持模式(已采集)");
            hand.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            hand.setText("手持模式(未采集)");
            hand.setTextColor(this.getResources().getColor(R.color.gray));

        }
        if (sharedPreferences.getString(state + "Pant Pocket", "").equals("clear")) {
            pocket.setText("口袋模式(已采集)");
            pocket.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            pocket.setText("口袋模式(未采集)");
            pocket.setTextColor(this.getResources().getColor(R.color.gray));
        }
        if (sharedPreferences.getString(state + "Hand Swing", "").equals("clear")) {
            arm.setText("手臂模式(已采集)");
            arm.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            arm.setText("手臂模式(未采集)");
            arm.setTextColor(this.getResources().getColor(R.color.gray));
        }
    }

    public void getView() {
        step_group = (RadioGroup) findViewById(R.id.step_group);
        hand = (RadioButton) findViewById(R.id.hand);
        pocket = (RadioButton) findViewById(R.id.pocket);
        arm = (RadioButton) findViewById(R.id.arm);
    }

    public void setOnClickListener() {
        hand.setOnClickListener(this);
        pocket.setOnClickListener(this);
        arm.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CollectWalkActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(CollectWalkActivity.this, LoadingActivity.class);
        switch (v.getId()) {
            case R.id.hand:
                AlertDialog.Builder builder0 = new AlertDialog.Builder(CollectWalkActivity.this);
                builder0.setTitle("提示").setMessage("手持手机并开始数据收集？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定按钮的点击事件
                        Bundle bundle = new Bundle();
                        bundle.putString("step", "Hand Fixed");
                        bundle.putString("state", CollectWalkActivity.this.getIntent().getStringExtra("state"));
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消按钮的点击事件
                    }
                }).show();
                break;
            case R.id.pocket:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CollectWalkActivity.this);
                builder1.setTitle("提示").setMessage("将手机放入口袋并开始数据收集？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定按钮的点击事件
                        Bundle bundle = new Bundle();
                        bundle.putString("step", "Pant Pocket");
                        bundle.putString("state", CollectWalkActivity.this.getIntent().getStringExtra("state"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消按钮的点击事件
                    }
                }).show();
                break;
            case R.id.arm:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(CollectWalkActivity.this);
                builder2.setTitle("提示").setMessage("将手机放入臂袋并开始数据收集？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定按钮的点击事件
                        Bundle bundle = new Bundle();
                        bundle.putString("step", "Hand Swing");
                        bundle.putString("state", CollectWalkActivity.this.getIntent().getStringExtra("state"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消按钮的点击事件
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
