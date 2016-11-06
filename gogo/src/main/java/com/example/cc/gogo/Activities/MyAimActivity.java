package com.example.cc.gogo.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.cc.gogo.R;

import butterknife.OnClick;

public class MyAimActivity extends AppCompatActivity implements View.OnClickListener {
  Button use_advice,set_by_self;
    TextView advice_step;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
    }
    public void getView(){
        use_advice=(Button)findViewById(R.id.use_advice);
        set_by_self=(Button)findViewById(R.id.set_by_self);
        advice_step = (TextView) findViewById(R.id.advice_step);
    }
    public void setOnClickListener()
    {
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
        switch (v.getId()){
            case R.id.set_by_self:
                builder =  new AlertDialog.Builder(MyAimActivity.this);
                builder.setTitle("选择强度");
                builder.setItems(new String[]{"很高", "高", "中", "低"}, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("which",which+"");
                    }
                });
                builder.show();
                builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("builder",position+"");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case R.id.use_advice:
                builder  = new AlertDialog.Builder(MyAimActivity.this);
                builder.setTitle("确认" ) ;
                builder.setMessage("已使用推荐设置！" ) ;
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定按钮的点击事件
                        advice_step.setText(2000+"步");
                    }
                });
                builder.show();
                break;
            default:break;

        }
    }
}
