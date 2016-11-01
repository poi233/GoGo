package com.example.cc.gogo.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cc.gogo.R;

public class LoadingActivity extends AppCompatActivity {
    com.shinelw.library.ColorArcProgressBar progressBar;
    String step,state;//上级传参
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar=(com.shinelw.library.ColorArcProgressBar)findViewById(R.id.progressBar);
        progressBar.setCurrentValues(100);

        Intent intent = this.getIntent();
         step = intent.getStringExtra("step");
         state=intent.getStringExtra("state");
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
}
