package com.example.cc.gogo.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.health.ServiceHealthStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.cc.gogo.R;
import com.example.cc.gogo.Service.StepCounterService;
import com.example.cc.gogo.svmlib.svm_predict;
import com.example.cc.gogo.svmlib.svm_scale;
import com.example.cc.gogo.svmlib.svm_train;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static com.example.cc.gogo.util.Constant.dir;
import static com.example.cc.gogo.util.Constant.modelFileBackupName;
import static com.example.cc.gogo.util.Constant.modelFileName;
import static com.example.cc.gogo.util.Constant.modelInfo;
import static com.example.cc.gogo.util.Constant.prdictInfo;
import static com.example.cc.gogo.util.Constant.range;
import static com.example.cc.gogo.util.Constant.resultFileName;
import static com.example.cc.gogo.util.Constant.scaleFileName;
import static com.example.cc.gogo.util.Constant.train;
import static java.io.File.separator;

public class CollectionMenuActivity extends AppCompatActivity implements OnClickListener {
    Button walk, run, recover, train;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getView();
        setOnClickListener();
        init();
        createTrainFile();
    }

    public void init() {
        //if (sharedPreferences == null) {    //SharedPreferences是Android平台上一个轻量级的存储类，
        //主要是保存一些常用的配置比如窗口状态
        sharedPreferences = getSharedPreferences("CollectStatus", MODE_PRIVATE);
        //}
        String runStatus = sharedPreferences.getString("Running", "");
        //Log.i("status", runStatus);
        String walkStatus = sharedPreferences.getString("Walking", "");
        //Log.i("status", walkStatus);
        if (runStatus.equals("clear")) {
            run.setBackgroundColor(this.getResources().getColor(R.color.black));
        } else {
            run.setBackgroundResource(R.drawable.step_btn_background);
        }
        if (walkStatus.equals("clear")) {
            walk.setBackgroundColor(this.getResources().getColor(R.color.black));
        } else {
            walk.setBackgroundResource(R.drawable.step_btn_background);
        }
        if (runStatus.equals("clear") && walkStatus.equals("clear")) {
            train.setVisibility(View.VISIBLE);
        } else {
            train.setVisibility(View.GONE);
        }
    }

    public void getView() {
        walk = (Button) findViewById(R.id.walk);
        run = (Button) findViewById(R.id.run);
        recover = (Button) findViewById(R.id.recover);
        train = (Button) findViewById(R.id.train);
    }

    public void setOnClickListener() {
        walk.setOnClickListener(this);
        run.setOnClickListener(this);
        recover.setOnClickListener(this);
        train.setOnClickListener(this);
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
        Intent intent = new Intent(this, CollectWalkActivity.class);
        switch (v.getId()) {
            case R.id.walk:
                if (StepCounterService.FLAG) {
                    AlertDialog.Builder warning = new AlertDialog.Builder(CollectionMenuActivity.this);
                    warning.setTitle("提示").setMessage("请关闭跑步计时后再进行数据采集").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定按钮的点击事件
                        }
                    }).show();
                } else {
                    bundle.putString("state", "Walking");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.run:
                if (StepCounterService.FLAG) {
                    AlertDialog.Builder warning = new AlertDialog.Builder(CollectionMenuActivity.this);
                    warning.setTitle("提示").setMessage("请关闭跑步计时后再进行数据采集").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定按钮的点击事件
                        }
                    }).show();
                } else {
                    bundle.putString("state", "Running");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.recover:
                try {
                    recover();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.train:
                tarinModel(dir + separator + "train",
                        dir + separator + range,
                        dir + separator + scaleFileName,
                        dir + separator + modelFileName,
                        dir + separator + train + separator + resultFileName,
                        dir + separator + train + separator + modelInfo,
                        dir + separator + train + separator + prdictInfo);
                break;
            default:
                break;
        }
    }

    private void recover() throws FileNotFoundException {
        sharedPreferences = getSharedPreferences("CollectStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        File file = new File(dir + File.separator + "train_self.txt");
        if (file.exists()) {
            file.delete();
        }
        resetModel();
        init();
        createTrainFile();
    }

    private void resetModel() throws FileNotFoundException {
        File file = new File(dir + separator + modelFileName);
        FileOutputStream fos = new FileOutputStream(dir + separator + modelFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            int len;
            byte[] b = new byte[1024];
            FileInputStream in = new FileInputStream(dir + separator + modelFileBackupName);
            while ((len = in.read(b)) != -1) {
                fos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void createTrainFile() {
        try {
            String fileName = "train_self.txt";
            copyStillTrainData(getAssets().open("train_still"), dir + separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyStillTrainData(InputStream in, String targetFilePath) {
        FileOutputStream fileOutputStream = null;
        File file = new File(targetFilePath);
        if (file.exists()) {        // 如果文件已经存在就结束
            return;
        }
        try {
            Log.i("fileCreate", "创建新文件" + targetFilePath);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(targetFilePath);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }
            Log.i("fileCreate", "创建成功" + file.getTotalSpace());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 训练模型
     *
     * @param trainFile
     * @param rangeFile
     * @param modelFile
     * @param result    显示精度结果
     */
    public void tarinModel(String trainFile, String rangeFile, String scaleFile, String modelFile, String result, String modelInfo, String prdictInfo) {
        createScaleFile(new String[]{"-l", "0", "-u", "1", "-s", rangeFile, trainFile}, scaleFile);
        creatModelFile(new String[]{"-s", "0", "-c", "128.0", "-t", "2", "-g", "8.0", "-e", "0.1", scaleFile, modelFile}, modelInfo);
        creatPredictFile(new String[]{scaleFile, modelFile, result}, prdictInfo);
        Toast.makeText(CollectionMenuActivity.this, "数据分析完成！", Toast.LENGTH_LONG).show();
        //svm_train.main(new String[]{"-s", "0", "-c", "128.0", "-t", "2", "-g", "8.0", "-e", "0.1", scaleFile, modelFile});
        //svm_predict.main(new String[]{scaleFile, modelFile, result});
    }


    /**
     * 训练数据train 进行归一化处理并生生scale文件
     *
     * @param args      String[] args = new String[]{"-l","0","-u","1",path+"/train"};
     * @param scalePath 结果输出文件路径
     */
    private static void createScaleFile(String[] args, String scalePath) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(scalePath);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_scale.main(args);//开始归一化
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatModelFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_train.main(args);//开始训练模型
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatPredictFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_predict.main(args);//开始测试精度
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }
}
