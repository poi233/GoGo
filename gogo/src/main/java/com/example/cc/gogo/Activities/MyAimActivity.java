package com.example.cc.gogo.Activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.cc.gogo.R;

public class MyAimActivity extends AppCompatActivity implements View.OnClickListener {
  Button use_advice,set_by_self;
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
                builder.setItems(new String[]{"很高", "高", "中", "低"}, null);
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
                builder.setPositiveButton("是" ,  null );
                builder.show();
                break;
            default:break;

        }
    }
}
