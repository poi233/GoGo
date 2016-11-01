package com.example.cc.gogo.Activities;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.example.cc.gogo.util.Constant.dir;
import android.app.AlertDialog;

public class CollectWalkActivity extends AppCompatActivity implements View.OnClickListener {
    FileOutputStream outputStream;
    RadioGroup step_group;
    RadioButton hand,pocket,arm;//获取被选取的RadioButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_walk);//获取界面组件
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createTrainFile();
        getView();
        setOnClickListener();
    }
    public void getView()
    {
        step_group=(RadioGroup) findViewById(R.id.step_group);
        hand= (RadioButton)findViewById(R.id.hand);
        pocket=(RadioButton)findViewById(R.id.pocket);
        arm=(RadioButton)findViewById(R.id.arm);
    }

    public void setOnClickListener(){
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

    private void createTrainFile() {
        try {
            String fileName = "train";
            fileName += ".txt";
            Log.i("info", "文件");
            File file = new File(dir + File.separator, fileName);
            if (file.exists()) {
                Log.i("info", "文件已存在");
                return;
            }
            Log.i("info", dir + File.separator + fileName);
            outputStream = new FileOutputStream(dir + File.separator + fileName, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        final Intent intent=new Intent(CollectWalkActivity.this,LoadingActivity.class);
        switch (v.getId()) {
                   case R.id.hand:
                       AlertDialog.Builder builder0 = new AlertDialog.Builder(CollectWalkActivity.this);
                       builder0.setTitle("提示").setMessage("手持手机并开始数据收集？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               //确定按钮的点击事件
                               Bundle bundle=new Bundle();
                               bundle.putString("step","hand");
                               bundle.putString("state",CollectWalkActivity.this.getIntent().getStringExtra("state"));
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
                               Bundle bundle=new Bundle();
                               bundle.putString("step","pocket");
                               bundle.putString("state",CollectWalkActivity.this.getIntent().getStringExtra("state"));
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
                               Bundle bundle=new Bundle();
                               bundle.putString("step","arm");
                               bundle.putString("state",CollectWalkActivity.this.getIntent().getStringExtra("state"));
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
            default:break;
        }
    }
}
