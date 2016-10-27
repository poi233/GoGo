package com.example.cc.gogo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import com.dd.CircularProgressButton;
import com.example.cc.gogo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.example.cc.gogo.util.Constant.dir;

public class CollectWalkActivity extends AppCompatActivity {
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_collect_walk);
        createTrainFile();

        final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                } else if (circularButton1.getProgress() == 100) {
                    CollectWalkActivity.this.finish();
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });
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
}
