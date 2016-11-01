package com.example.cc.gogo.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.cc.gogo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.example.cc.gogo.util.Constant.dir;

public class CollectionMenuActivity extends AppCompatActivity implements OnClickListener {
    Button walk, run, still;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
    }

    public void getView() {
        walk = (Button) findViewById(R.id.walk);
        run = (Button) findViewById(R.id.run);
        still = (Button) findViewById(R.id.still);
    }

    public void setOnClickListener() {
        walk.setOnClickListener(this);
        run.setOnClickListener(this);
        still.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CollectionMenuActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Intent intent=new Intent(this, CollectWalkActivity.class);
        switch (v.getId()) {
            case R.id.walk:

                bundle.putString("state", "walk");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.run:
                bundle.putString("state", "run");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.still:
                break;
            default:break;
        }
    }
}
